package com.example.twitch;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/demo")
public class demo {

    @GetMapping()
    public String demo() {

        return "demooo";
    }

}
