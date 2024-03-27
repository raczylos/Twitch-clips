package com.example.twitch.clip;

import com.example.twitch.auth.AuthenticationResponse;
import com.example.twitch.auth.TwitchUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/twitch/clips")
public class ClipController {
    private final ClipService clipService;

    @Autowired
    public ClipController(ClipService clipService) {
        this.clipService = clipService;
    }

    @GetMapping
    public List<Clip> getClips() {
        return clipService.getClips();
    }


//    @GetMapping("/streamerPopularClips")
//    public ResponseEntity<List<Clip>> getStreamerPopularClips(@RequestParam("accessToken") String accessToken, @RequestParam("started_at") String startedAt, @RequestParam("ended_at") String endedAt) {
////        clipService.streamerPopularClips(accessToken, "xayoo_");
//
//        return ResponseEntity.ok(clipService.streamerPopularClips(accessToken, "xayoo_", startedAt, endedAt));
//    }

    @GetMapping("/{streamerId}")
    public ResponseEntity<List<Clip>> getStreamerPopularClips(@RequestParam("accessToken") String accessToken, @PathVariable String streamerId, @RequestParam("started_at") String startedAt, @RequestParam("ended_at") String endedAt) {
//        clipService.streamerPopularClips(accessToken, "xayoo_");

        return ResponseEntity.ok(clipService.streamerPopularClips(accessToken, streamerId, startedAt, endedAt, 100));
    }

    @GetMapping("/followed")
    public ResponseEntity<List<Clip>> getFollowedStreamersPopularClips(@RequestParam("accessToken") String accessToken, @RequestParam("login") String login, @RequestParam("started_at") String startedAt, @RequestParam("ended_at") String endedAt) {
        return ResponseEntity.ok(clipService.followedStreamersClips(accessToken, login, startedAt, endedAt));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Clip>> getPopularSteamersClips(@RequestParam("accessToken") String accessToken, @RequestParam("started_at") String startedAt, @RequestParam("ended_at") String endedAt) {
        return ResponseEntity.ok(clipService.popularStreamersClips(accessToken, startedAt, endedAt));
    }





}
