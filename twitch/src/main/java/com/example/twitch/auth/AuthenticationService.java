package com.example.twitch.auth;

import com.example.twitch.config.JwtService;
import com.example.twitch.token.*;
import com.example.twitch.user.UserType;
import com.example.twitch.user.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;


@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;
    private final TwitchUserRepository twitchUserRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    private final RestTemplate restTemplate;

    @Value("${twitch-client-id}")
    private String twitchClientId;

    @Value("${twitch-client-secret}")
    private String twitchClientSecret;


    public AuthenticationService(UserRepository userRepository,
                                 TokenRepository tokenRepository, TwitchUserRepository twitchUserRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService, TokenService tokenService, AuthenticationManager authenticationManager
                                 ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.twitchUserRepository = twitchUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.restTemplate =  new RestTemplate();

    }


    public AuthenticationResponse register(RegisterRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        var user = new NormalUser(request.getUsername(), request.getEmail(), encodedPassword, Role.USER, UserType.User);
        var savedUser = userRepository.save(user);
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(accessToken, savedUser, Type.AccessToken);
        saveUserToken(refreshToken, savedUser, Type.RefreshToken);
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
        saveUserToken(accessToken, user, Type.AccessToken);
        saveUserToken(refreshToken, user, Type.RefreshToken);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    private void saveUserToken(String token, User user, Type type) {
        var tokenToSave = new Token(token, TokenType.BEARER, false, false, user, type);
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

    private void unrevokeToken(String token, Type type) {
        var userToken = tokenRepository.findByTokenAndType(token, type);
        if(userToken.isEmpty()) {
            return;
        }
        userToken.get().setExpired(false);
        userToken.get().setRevoked(false);

        tokenRepository.save(userToken.get());

    }


    public ResponseEntity<AuthenticationResponse> refreshToken(String bearerToken) {
        if (bearerToken == null) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Authorization header is missing")
                    .build();
        }

        if (!bearerToken.startsWith("Bearer ")) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Authorization header must start with Bearer")
                    .build();
        }

        String refreshToken = bearerToken.substring(7);
        String username = jwtService.extractUsername(refreshToken);


        if (username == null) {
            return ResponseEntity
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .header("Error", "Username in token not found")
                    .build();
        }

        var user = this.userRepository.findByEmail(username).orElse(null);
        var twitchUser = this.twitchUserRepository.findByEmail(username).orElse(null);
        var specificUser = (user != null) ? user : twitchUser;

        if (specificUser == null) {
            return ResponseEntity
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .header("Error", "User not found")
                    .build();
        }

        if (!jwtService.isTokenValid(refreshToken, specificUser) || !tokenService.isTokenInDBValid(refreshToken, Type.RefreshToken)) {
            return ResponseEntity
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .header("Error", "Invalid refresh token")
                    .build();
        }

        var accessToken = jwtService.generateToken(specificUser);
        revokeAllUserTokens(specificUser);
        unrevokeToken(refreshToken, Type.RefreshToken);
        saveUserToken(accessToken, specificUser, Type.AccessToken);

        var authResponse = new AuthenticationResponse(accessToken, refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    public AuthenticationResponse authenticateTwitchUser(String twitchAccessToken, String twitchRefreshToken) {
        var twitchUserData = getTwitchUserInfo(twitchAccessToken, twitchRefreshToken).getData()[0];

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
            saveUserToken(accessToken, user, Type.AccessToken);
            saveUserToken(refreshToken, user, Type.RefreshToken);

            return new AuthenticationResponse(accessToken, refreshToken);
        } else {
            twitchUserRepository.save(twitchUser);

            var accessToken = jwtService.generateToken(twitchUser);
            var refreshToken = jwtService.generateRefreshToken(twitchUser);

            revokeAllUserTokens(twitchUser);
            saveUserToken(accessToken, twitchUser, Type.AccessToken);
            saveUserToken(refreshToken, twitchUser, Type.RefreshToken);

            return new AuthenticationResponse(accessToken, refreshToken);
        }
    }

    public TwitchUsersResponse getTwitchUserInfo(String twitchAccessToken, String twitchRefreshToken) {

        var response = validateTwitchAccessToken(twitchAccessToken);

//        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
//            var refreshResponse = refreshTwitchAccessToken(twitchRefreshToken);
//            twitchAccessToken = refreshResponse.getBody().getAccessToken();
//            twitchRefreshToken = refreshResponse.getBody().getRefreshToken();
//        }



        String twitchApiUrl = "https://api.twitch.tv/helix/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(twitchAccessToken);
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

    public ResponseEntity<String> validateTwitchAccessToken(String twitchAccessToken) {
        String twitchValidateUrl = "https://id.twitch.tv/oauth2/validate";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(twitchAccessToken);


        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                twitchValidateUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response;
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

    public TwitchTokensResponse refreshTwitchAccessToken(String twitchRefreshToken) {
        String twitchTokenUrl = "https://id.twitch.tv/oauth2/token";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(twitchTokenUrl)
                .queryParam("client_id", twitchClientId)
                .queryParam("client_secret", twitchClientSecret)
                .queryParam("refresh_token", twitchRefreshToken)
                .queryParam("grant_type", "refresh_token");

        ResponseEntity<TwitchTokensResponse> tokensResponse = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                null,
                TwitchTokensResponse.class
        );

//        if(tokensResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
//            System.out.println(tokensResponse.getBody());
//            return null;
//        }

        return tokensResponse.getBody();
    }



}
