package com.example.twitch.follower;

public record FollowerDto(
        Integer twitchUserId,
        Integer streamerId,
        String twitchUserLogin,
        String streamerDisplayName
) {
}
