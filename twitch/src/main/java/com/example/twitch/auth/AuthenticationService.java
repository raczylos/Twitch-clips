package com.example.twitch.auth;

import com.example.twitch.config.JwtService;
import com.example.twitch.user.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.constant.Constable;
import java.net.URI;
import java.util.List;
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
        TwitchUser twitchUser = getTwitchUserInfo(twitchAccessToken);

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

    public TwitchUser getTwitchUserInfo(String token) {

        String twitchApiUrl = "https://api.twitch.tv/helix/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Client-Id", twitchClientId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(twitchApiUrl);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TwitchUserResponse> twitchUserResponse = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TwitchUserResponse.class
        );




        var twitchUserResponseData = twitchUserResponse.getBody().getData();

        TwitchUser twitchUser = new TwitchUser(
                twitchUserResponseData[0].getLogin(),
                twitchUserResponseData[0].getEmail(),
                twitchUserResponseData[0].getId(),
                Role.USER
        );

        return twitchUser;
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
