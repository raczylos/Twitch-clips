package com.example.twitch;

import com.example.twitch.clip.Clip;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/demo")
public class demo {

    @GetMapping()
    public String getStreamerPopularClips() {

        return "demooo";
    }

}
