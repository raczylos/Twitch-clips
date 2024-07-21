package com.example.twitch.streamer;

import com.example.twitch.auth.TwitchUsersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/twitch/streamer")
public class StreamerController {

    private final StreamerService streamerService;

    public StreamerController(StreamerService streamerService) {
        this.streamerService = streamerService;
    }



    @PostMapping("/add")
    public ResponseEntity<Streamer> addStreamer(@RequestParam("accessToken") String accessToken, @RequestParam("login") String login) {

        return ResponseEntity.ok(streamerService.addStreamer(accessToken, login));
    }

    @PostMapping("/addAll")
    public ResponseEntity<List<Streamer>> addAllStreamers(@RequestParam("accessToken") String accessToken) {

        return ResponseEntity.ok(streamerService.addAllStreamers(accessToken));
    }


}
