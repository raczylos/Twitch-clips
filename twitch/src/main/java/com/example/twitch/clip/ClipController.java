package com.example.twitch.clip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/all")
    public List<Clip> getAllClips() {
        return clipService.getAllClips();
    }

    @GetMapping()
    public List<Clip> getClipsByPage(@RequestParam int page,
                               @RequestParam int size) {
        return clipService.getClipsByPage(page, size);
    }

    @GetMapping("/{id}")
    public Clip getClip(@PathVariable("id") String clipId) {
        return clipService.getClip(clipId);
    }

    @GetMapping("/{streamerId}")
    public ResponseEntity<List<ClipDto>> getStreamerPopularClips(@RequestParam("accessToken") String accessToken, @PathVariable String streamerId, @RequestParam("started_at") String startedAt, @RequestParam("ended_at") String endedAt) {
        return ResponseEntity.ok(clipService.streamerPopularClips(accessToken, streamerId, startedAt, endedAt, 100));
    }

    @GetMapping("/followed")
    public ResponseEntity<List<ClipDto>> getFollowedStreamersPopularClips(@RequestParam("accessToken") String accessToken, @RequestParam("login") String login, @RequestParam("started_at") String startedAt, @RequestParam("ended_at") String endedAt) {
        return ResponseEntity.ok(clipService.addFollowedStreamersClips(accessToken, login, startedAt, endedAt));
    }
    @GetMapping("/popular")
    public ResponseEntity<Page<ClipDto>> getAllPopularClips(@RequestParam("startedAt") String startedAt, @RequestParam("endedAt") String endedAt, @RequestParam int page,
                                                              @RequestParam int pageSize ) {
        return ResponseEntity.ok(clipService.getPopularClips(startedAt, endedAt, page, pageSize));
    }
    @PostMapping("/system/popular")
    public ResponseEntity<List<ClipDto>> addPopularSteamersClipsBySystem(@RequestParam("accessToken") String accessToken, @RequestParam("started_at") String startedAt, @RequestParam("ended_at") String endedAt) {
        return ResponseEntity.ok(clipService.addPopularStreamersClipsBySystem(accessToken, startedAt, endedAt));
    }

}
