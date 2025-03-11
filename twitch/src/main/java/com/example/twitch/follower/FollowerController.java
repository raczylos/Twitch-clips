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
    public ResponseEntity<FollowerDto> addFollower(@RequestParam("twitchUserId") Integer twitchUserId, @RequestParam("streamerId") Integer streamerId) {

        return ResponseEntity.ok(followerService.addFollower(twitchUserId, streamerId));
    }

    @PostMapping("/addAll")
    public ResponseEntity<List<FollowerDto>> addAllUserFollows(@RequestParam("userLogin") String userLogin, @RequestParam("twitchAccessToken") String twitchAccessToken) {

        return ResponseEntity.ok(followerService.addAllUserFollows(userLogin, twitchAccessToken));
    }
}
