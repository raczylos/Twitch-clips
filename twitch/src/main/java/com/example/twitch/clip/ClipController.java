package com.example.twitch.clip;

import com.example.twitch.auth.AuthenticationResponse;
import com.example.twitch.auth.TwitchUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/clips")
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


    @GetMapping("/streamerPopularClips")
    public ResponseEntity<TwitchClipsResponse> getStreamerPopularClips(@RequestParam("accessToken") String accessToken, @RequestParam("started_at") String startedAt, @RequestParam("ended_at") String endedAt) {
//        clipService.streamerPopularClips(accessToken, "xayoo_");

        return ResponseEntity.ok(clipService.streamerPopularClips(accessToken, "xayoo_", startedAt, endedAt));
    }

    @GetMapping("/getTwitchUserInfoByLogin")
    public ResponseEntity<TwitchUsersResponse> getTwitchUserInfoByLogin(@RequestParam("accessToken") String accessToken) {

        return ResponseEntity.ok(clipService.getTwitchUserInfoByLogin(accessToken, "lightt__"));
    }



}
