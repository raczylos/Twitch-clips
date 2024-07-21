package com.example.twitch.auth;

import com.example.twitch.config.JwtService;
import com.example.twitch.user.TwitchUserRepository;
import com.example.twitch.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final TwitchUserRepository twitchUserRepository;

    public AuthenticationController(AuthenticationService service, JwtService jwtService, UserRepository userRepository, TwitchUserRepository twitchUserRepository) {
        this.service = service;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.twitchUserRepository = twitchUserRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (
        @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @PostMapping("/refresh-token")
    public void refreshToken (
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }



    @PostMapping("/validate/twitch")
    public ResponseEntity<String> validateTwitchToken(@RequestParam("accessToken") String accessToken) {
        try {
            return service.validateTwitchAccessToken(accessToken);
        } catch (HttpClientErrorException.Unauthorized ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token");
        }

    }


    @GetMapping("/login/oauth2/code/twitch")
    public RedirectView loginByTwitch(@RequestParam("code") String code) {

        var twitchTokens = service.getTwitchTokens(code);

        var tokens = service.authenticateTwitchUser(twitchTokens.getAccessToken(), twitchTokens.getRefreshToken());

        System.out.println("accessToken " + tokens.getAccessToken());
        System.out.println("refreshToken " + tokens.getRefreshToken());

        String redirectUrl = "http://127.0.0.1:5173/login?twitchAccessToken=" + twitchTokens.getAccessToken() +
                "&twitchRefreshToken=" + twitchTokens.getRefreshToken() + "&accessToken=" + tokens.getAccessToken() + "&refreshToken=" + tokens.getRefreshToken();

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUrl);
        return redirectView;

    }


    @GetMapping("/twitch/tokens")
    public ResponseEntity<TwitchTokensResponse> getTwitchTokens(@RequestParam("code") String code) {
        var tokens = service.getTwitchTokens(code);

        System.out.println(tokens);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/twitch/refresh-token")
    public ResponseEntity<TwitchTokensResponse> refreshTwitchAccessToken(@RequestParam("twitchRefreshToken") String twitchRefreshToken) {
        var tokens = service.refreshTwitchAccessToken(twitchRefreshToken);

        System.out.println(tokens);

        return ResponseEntity.ok(tokens);
    }



}
