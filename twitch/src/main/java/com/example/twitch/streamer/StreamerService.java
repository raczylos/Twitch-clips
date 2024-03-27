package com.example.twitch.streamer;

import com.example.twitch.user.TwitchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StreamerService {

    private final StreamerRepository streamerRepository;

    private final TwitchUserService twitchUserService;


    @Autowired
    public StreamerService(StreamerRepository streamerRepository, TwitchUserService twitchUserService) {
        this.streamerRepository = streamerRepository;
        this.twitchUserService = twitchUserService;
    }

    public Streamer getStreamer(String login) {
        var streamer = streamerRepository.findByLogin(login);
        if(streamer.isEmpty()) {
            System.out.println("Streamer doesn't exist in database");
            return null;
        }

        return streamer.get();
    }

    public Streamer addStreamer(String token, String login) {

        var existingStreamer = streamerRepository.findByLogin(login);

        if(existingStreamer.isPresent()){
            System.out.println("Streamer already exists in database");
            return null;
        }

        var data = twitchUserService.getTwitchUserInfoByLogin(token, login).getData();

        if(data.length == 0) {
            System.out.println("Invalid login of streamer");
            System.out.println(login);
            return null;
        }

        var streamer = new Streamer(data[0].getLogin(), data[0].getDisplayName(), data[0].getId(), data[0].getProfileImageUrl());
        streamerRepository.save(streamer);

        return streamer;

    }

    public List<Streamer> addAllStreamer(String token) {

        List<Streamer>  streamerList = new ArrayList<>();
        for(var streamerLogin : StreamerList.values()) {
            if(streamerLogin != null) {
                var streamer = addStreamer(token, streamerLogin.toString());
                streamerList.add(streamer);
            }

        }

        return streamerList;
    }

}
