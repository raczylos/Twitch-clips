package com.example.twitch.streamer;

import com.example.twitch.user.TwitchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.twitch.streamer.StreamerException.*;

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

    public Streamer getStreamerByStreamerId(String streamerId) {
        var streamer = streamerRepository.findByTwitchId(streamerId);
        if(streamer.isEmpty()) {
            System.out.println("Streamer doesn't exist in database");
            return null;
        }

        return streamer.get();
    }

    public Streamer addStreamer(String token, String login) {

        var existingStreamer = streamerRepository.findByLogin(login);

        if(existingStreamer.isPresent()){
            throw new StreamerAlreadyExistsException(String.format("Streamer already exists in database: %s", login));
        }

        var data = twitchUserService.getTwitchUserInfoByLogin(token, login).getData();

        if(data.length == 0) {
            throw new InvalidStreamerLoginException(String.format("Invalid login of streamer: %s", login));
        }

        var streamer = new Streamer(data[0].getLogin(), data[0].getDisplayName(), data[0].getId(), data[0].getProfileImageUrl(), null);
        streamerRepository.save(streamer);

        return streamer;

    }

    public List<Streamer> addAllStreamers(String token) {

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
