package com.example.twitch.auth;

import com.example.twitch.user.User;
import com.example.twitch.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5173")
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


    @GetMapping("/validate/twitch")
    public ResponseEntity<String> validateTwitchToken(@RequestParam("accessToken") String accessToken) {
        return ResponseEntity.ok(service.validateTwitchAccessToken(accessToken));
    }


    @GetMapping("/login/oauth2/code/twitch")
    public RedirectView loginByTwitch(@RequestParam("code") String code) {

        var tokens = service.getTwitchTokens(code);

        var jwtToken = service.authenticateTwitchUser(tokens.getAccessToken()).getToken();

        System.out.println("jwtToken " + jwtToken);


        String redirectUrl = "http://127.0.0.1:5173/login?twitchAccessToken=" + tokens.getAccessToken() +
                "&twitchRefreshToken=" + tokens.getRefreshToken() + "&jwtToken=" + jwtToken;

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



}
