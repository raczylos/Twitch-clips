package com.example.twitch.clip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClipService {

    private final ClipRepository clipRepository;

    @Autowired
    public ClipService(ClipRepository clipRepository) {
        this.clipRepository = clipRepository;
    }

    public List<Clip> getClips() {
        return clipRepository.findAll();

    }
}
