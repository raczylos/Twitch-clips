package com.example.twitch.streamer;

import com.example.twitch.user.TwitchUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.twitch.streamer.StreamerException.InvalidStreamerLoginException;
import static com.example.twitch.streamer.StreamerException.StreamerAlreadyExistsException;

@Service
public class StreamerService {

    private static final Logger logger = LoggerFactory.getLogger(StreamerService.class);
    private final StreamerRepository streamerRepository;
    private final TwitchUserService twitchUserService;

    @Autowired
    public StreamerService(StreamerRepository streamerRepository, TwitchUserService twitchUserService) {
        this.streamerRepository = streamerRepository;
        this.twitchUserService = twitchUserService;
    }

    public Streamer getStreamer(String login) {
        var streamer = streamerRepository.findByLogin(login);
        if (streamer.isEmpty()) {
            System.out.println("Streamer doesn't exist in database");
            return null;
        }

        return streamer.get();
    }

    public Streamer getStreamerByStreamerId(String streamerId) {
        var streamer = streamerRepository.findByTwitchId(streamerId);
        if (streamer.isEmpty()) {
            System.out.println("Streamer doesn't exist in database");
            return null;
        }

        return streamer.get();
    }

    public Streamer addStreamer(String token, String login) {

        var existingStreamer = streamerRepository.findByLogin(login);

        if (existingStreamer.isPresent()) {
            throw new StreamerAlreadyExistsException(String.format("Streamer already exists in database: %s", login));
        }

        var data = twitchUserService.getTwitchUserInfoByLogin(token, login).getData();

        if (data.length == 0) {
            throw new InvalidStreamerLoginException(String.format("Invalid login of streamer: %s", login));
        }

        var streamer = new Streamer(data[0].getLogin(), data[0].getDisplayName(), data[0].getId(), data[0].getProfileImageUrl());
        streamerRepository.save(streamer);

        return streamer;

    }

    public List<Streamer> addAllStreamers(String token) {
        List<Streamer> streamerList = new ArrayList<>();
        for (var streamerLogin : StreamerList.values()) {
            if (streamerLogin != null) {
                try {
                    var streamer = addStreamer(token, streamerLogin.toString());
                    streamerList.add(streamer);
                } catch (StreamerAlreadyExistsException e) {
                    logger.error("Streamer already exists: {}", streamerLogin);
                } catch (InvalidStreamerLoginException e) {
                    logger.error("Invalid streamer login: {}", streamerLogin);
                } catch (Exception e) {
                    logger.error("Unexpected error adding streamer {}: {}", streamerLogin, e.getMessage());
                }
            }
        }

        return streamerList;
    }

}
