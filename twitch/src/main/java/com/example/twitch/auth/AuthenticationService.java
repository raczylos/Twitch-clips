package com.example.twitch.auth;

import com.example.twitch.config.JwtService;
import com.example.twitch.user.*;
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
                                 TwitchUserRepository twitchUserRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager
                                 ) {
        this.userRepository = userRepository;
        this.twitchUserRepository = twitchUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.restTemplate =  new RestTemplate();

    }


    public AuthenticationResponse register(RegisterRequest request) {

        var user = new User(request.getUsername(), request.getEmail(), request.getPassword(), Role.USER);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticateTwitchUser(String twitchAccessToken) {
        var twitchUserData = getTwitchUserInfo(twitchAccessToken).getData()[0];

        TwitchUser twitchUser = new TwitchUser(
                twitchUserData.getLogin(),
                twitchUserData.getEmail(),
                twitchUserData.getId(),
                Role.USER
        );

        Optional<TwitchUser> existingUser = twitchUserRepository.findByEmail(twitchUser.getEmail());

        if (existingUser.isPresent()) {
            TwitchUser user = existingUser.get();
            String jwtToken = jwtService.generateToken(user);

            return new AuthenticationResponse(jwtToken);
        } else {
            twitchUserRepository.save(twitchUser);

            String jwtToken = jwtService.generateToken(twitchUser);

            return new AuthenticationResponse(jwtToken);
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
