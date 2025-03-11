package com.example.twitch.follower;

import com.example.twitch.streamer.Streamer;
import com.example.twitch.streamer.StreamerException.InvalidStreamerLoginException;
import com.example.twitch.streamer.StreamerException.StreamerAlreadyExistsException;
import com.example.twitch.streamer.StreamerRepository;
import com.example.twitch.streamer.StreamerService;
import com.example.twitch.user.TwitchUserRepository;
import com.example.twitch.user.TwitchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final StreamerService streamerService;
    private final TwitchUserService twitchUserService;
    private final TwitchUserRepository twitchUserRepository;
    private final StreamerRepository streamerRepository;
    private final FollowerMapper followerMapper;

    @Autowired
    public FollowerService(FollowerRepository followerRepository, StreamerService streamerService, TwitchUserService twitchUserService, TwitchUserRepository twitchUserRepository, StreamerRepository streamerRepository, FollowerMapper followerMapper) {
        this.followerRepository = followerRepository;
        this.streamerService = streamerService;
        this.twitchUserService = twitchUserService;
        this.twitchUserRepository = twitchUserRepository;
        this.streamerRepository = streamerRepository;
        this.followerMapper = followerMapper;
    }

    public List<Follower> getFollowers(Integer userId) {
        var followers = followerRepository.findFollowersByTwitchUserId(userId);
        if (followers.isEmpty()) {
            System.out.println("User doesn't have any followers");
            return null;
        }

        return followers;
    }

    public FollowerDto addFollower(Integer twitchUserId, Integer streamerId) {
        var follower = followerRepository.findByStreamerIdAndTwitchUserId(streamerId, twitchUserId);
        if (follower.isPresent()) {
            System.out.println("Follower already exists in database");
            return null;
        }
        var newFollower = new Follower(twitchUserRepository.findById(twitchUserId).get(),
                streamerRepository.findById(streamerId).get());
        followerRepository.save(newFollower);

        return followerMapper.entityToFollowerDto(newFollower);
    }

    public List<FollowerDto> addAllUserFollows(String userLogin, String twitchAccessToken) {

        var user = twitchUserService.getTwitchUserByLogin(userLogin);
        if(user.isPresent()) {
            //            TODO ERROR MESSAGE
            System.out.println("User doesn't exists in database");
            return null;
        }
        var follows = twitchUserService.getTwitchUserFollows(twitchAccessToken, userLogin);
        if(follows == null) {
            //            TODO ERROR MESSAGE
            System.out.println("User doesn't have any followers");
            return null;
        }
        List<FollowerDto> followers = new ArrayList<>();
        for (var follow : follows) {
            Streamer streamer;
            try {
                streamer = streamerService.addStreamer(twitchAccessToken, follow.getStreamerLogin());
            } catch (StreamerAlreadyExistsException e) {
                streamer = streamerService.getStreamer(follow.getStreamerLogin());
            } catch (InvalidStreamerLoginException e) {
                System.out.println(e.getMessage());
                continue;
            }

            if (streamer != null) {
                FollowerDto newFollower = addFollower(user.get().getId(), streamer.getId());
                if (newFollower != null) {
                    followers.add(newFollower);
                }
            }


        }

        return followers;
    }

}
