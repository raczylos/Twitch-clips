package com.example.twitch.follower;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/twitch/follower")
public class FollowerController {

    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PostMapping("/add")
    public ResponseEntity<Follower> addFollower(@RequestParam("twitchUserId") Integer twitchUserId, @RequestParam("streamerId") Integer streamerId) {

        return ResponseEntity.ok(followerService.addFollower(twitchUserId, streamerId));
    }

    @PostMapping("/addAll")
    public ResponseEntity<List<Follower>> addFollowers(@RequestParam("userLogin") String userLogin, @RequestParam("twitchAccessToken") String twitchAccessToken, @RequestParam("twitchRefreshToken") String twitchRefreshToken) {

        return ResponseEntity.ok(followerService.addFollowers(userLogin, twitchAccessToken, twitchRefreshToken));
    }
}
