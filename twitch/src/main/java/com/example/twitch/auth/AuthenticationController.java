package com.example.twitch.auth;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;



@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
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
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestParam(value = "refreshToken") String refreshToken
    ) {
        return service.refreshToken(refreshToken);
    }

    @PostMapping("/validate/twitch")
    public ResponseEntity<String> validateTwitchToken(@RequestParam("accessToken") String accessToken) {
        try {
            ResponseEntity<String> response = service.validateTwitchAccessToken(accessToken);
            return new ResponseEntity<>(response.getBody(), response.getStatusCode());
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
