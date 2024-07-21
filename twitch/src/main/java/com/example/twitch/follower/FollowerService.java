package com.example.twitch.follower;

import com.example.twitch.streamer.Streamer;
import com.example.twitch.streamer.StreamerService;
import com.example.twitch.streamer.streamerException.*;
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

    @Autowired
    public FollowerService(FollowerRepository followerRepository, StreamerService streamerService, TwitchUserService twitchUserService) {
        this.followerRepository = followerRepository;
        this.streamerService = streamerService;
        this.twitchUserService = twitchUserService;
    }

    public List<Follower> getFollowers(Long userId) {
        var followers = followerRepository.findFollowersByUserId(userId);
        if(followers.isEmpty()) {
            System.out.println("User doesn't have any followers");
            return null;
        }

        return followers;
    }

    public Follower addFollower(Long userId, Long streamerId, String streamerLogin) {
        var follower = followerRepository.findByStreamerIdAndUserId(streamerId, userId);
        if(follower.isPresent()){
            System.out.println("Follower already exists in database");
            return null;
        }
        var newFollower = new Follower(userId, streamerId, streamerLogin);
        followerRepository.save(newFollower);

        return newFollower;
    }

    public List<Follower> addFollowers(String userLogin, String twitchAccessToken, String twitchRefreshToken) {

        var userId = twitchUserService.getTwitchUserByLogin(userLogin).get().getId();
        var follows = twitchUserService.getTwitchUserFollows(twitchAccessToken, twitchRefreshToken, userLogin);
        List<Follower> followers = new ArrayList<>();
        for(var follow: follows.getData()) {
            Streamer streamer;
            try{
                streamer = streamerService.addStreamer(twitchAccessToken, follow.getStreamerLogin());
            } catch (StreamerAlreadyExistsException e) {
                streamer = streamerService.getStreamer(follow.getStreamerLogin());
            } catch(InvalidStreamerLoginException e) {
                System.out.println(e.getMessage());
                continue;
            }

            if(streamer != null){
                var newFollower = addFollower(userId, streamer.getId(), streamer.getLogin());
                if(newFollower != null){
                    followers.add(newFollower);
                }
            }


        }

        return followers;
    }

}
