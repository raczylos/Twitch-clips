package com.example.twitch.user;

import com.example.twitch.auth.TwitchUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/twitch/user")
public class TwitchUserController {

    private final TwitchUserService twitchUserService;

    @Autowired
    public TwitchUserController(TwitchUserService twitchUserService) {
        this.twitchUserService = twitchUserService;
    }


    @GetMapping("/follows")
    public ResponseEntity<TwitchUserFollowsResponse> getTwitchUserFollows(@RequestParam("twitchAccessToken") String accessToken, @RequestParam("twitchRefreshToken") String twitchRefreshToken, @RequestParam("login") String login) {
        return ResponseEntity.ok(twitchUserService.getTwitchUserFollows(accessToken, twitchRefreshToken, login));
    }

    @GetMapping("/info")
    public ResponseEntity<TwitchUsersResponse> getTwitchUserInfoByLogin(@RequestParam("accessToken") String accessToken, @RequestParam("login") String login) {

        return ResponseEntity.ok(twitchUserService.getTwitchUserInfoByLogin(accessToken, login));
    }


}
