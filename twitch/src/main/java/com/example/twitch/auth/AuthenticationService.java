package com.example.twitch.auth;

import com.example.twitch.config.JwtService;
import com.example.twitch.token.Token;
import com.example.twitch.token.TokenRepository;
import com.example.twitch.token.TokenType;
import com.example.twitch.user.UserType;
import com.example.twitch.user.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;


@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;
    private final TwitchUserRepository twitchUserRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final RestTemplate restTemplate;

    @Value("${twitch-client-id}")
    private String twitchClientId;

    @Value("${twitch-client-secret}")
    private String twitchClientSecret;


    public AuthenticationService(UserRepository userRepository,
                                 TokenRepository tokenRepository, TwitchUserRepository twitchUserRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager
                                 ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.twitchUserRepository = twitchUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.restTemplate =  new RestTemplate();

    }


    public AuthenticationResponse register(RegisterRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        var user = new NormalUser(request.getUsername(), request.getEmail(), encodedPassword, Role.USER, UserType.User);
        var savedUser = userRepository.save(user);
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(accessToken, savedUser);
        return new AuthenticationResponse(accessToken, refreshToken);
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(accessToken, user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    private void saveUserToken(String token, User user) {
        var tokenToSave = new Token(token, TokenType.BEARER, false, false, user);
        tokenRepository.save(tokenToSave);
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    public void refreshToken (
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            return;
        }
        if (authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);

            String username = jwtService.extractUsername(refreshToken);

            if (username != null) {
                var user = this.userRepository.findByEmail(username).orElse(null);
                var twitchUser = this.twitchUserRepository.findByEmail(username).orElse(null);

                var specificUser = (user != null) ? user : twitchUser;

                if (jwtService.isTokenValid(refreshToken, specificUser)) {
                    var accessToken = jwtService.generateToken(specificUser);

                    revokeAllUserTokens(specificUser);
                    saveUserToken(accessToken, specificUser);

                    var authResponse = new AuthenticationResponse(accessToken, refreshToken);
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                }

            }
        }

    }

    public AuthenticationResponse authenticateTwitchUser(String twitchAccessToken) {
        var twitchUserData = getTwitchUserInfo(twitchAccessToken).getData()[0];

        TwitchUser twitchUser = new TwitchUser(
                twitchUserData.getLogin(),
                twitchUserData.getEmail(),
                twitchUserData.getId(),
                Role.USER,
                UserType.TwitchUser
        );

        Optional<TwitchUser> existingUser = twitchUserRepository.findByEmail(twitchUser.getEmail());

        if (existingUser.isPresent()) {
            TwitchUser user = existingUser.get();
            var accessToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

            revokeAllUserTokens(user);
            saveUserToken(accessToken, user);

            return new AuthenticationResponse(accessToken, refreshToken);
        } else {
            twitchUserRepository.save(twitchUser);

            var accessToken = jwtService.generateToken(twitchUser);
            var refreshToken = jwtService.generateRefreshToken(twitchUser);

            revokeAllUserTokens(twitchUser);
            saveUserToken(accessToken, twitchUser);

            return new AuthenticationResponse(accessToken, refreshToken);
        }
    }

    public TwitchUsersResponse getTwitchUserInfo(String token) {

        String twitchApiUrl = "https://api.twitch.tv/helix/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Client-Id", twitchClientId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(twitchApiUrl);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TwitchUsersResponse> twitchUserResponse = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TwitchUsersResponse.class
        );




        var twitchUserResponseData = twitchUserResponse.getBody();



        return twitchUserResponseData;
    }

    public String validateTwitchAccessToken(String token) {
        String twitchValidateUrl = "https://id.twitch.tv/oauth2/validate";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);


        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                twitchValidateUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }

    public TwitchTokensResponse getTwitchTokens(String code) {
        String twitchTokenUrl = "https://id.twitch.tv/oauth2/token";

        String twitchRedirectUri = "http://localhost:8080/api/v1/auth/login/oauth2/code/twitch";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(twitchTokenUrl)
                .queryParam("client_id", twitchClientId)
                .queryParam("client_secret", twitchClientSecret)
                .queryParam("code", code)
                .queryParam("redirect_uri", twitchRedirectUri)
                .queryParam("grant_type", "authorization_code");


        TwitchTokensResponse tokensResponse = restTemplate.postForObject(builder.toUriString(), null, TwitchTokensResponse.class);

        return tokensResponse;
    }










}
