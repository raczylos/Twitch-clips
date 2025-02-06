package com.example.twitch.streamer;

public record StreamerDto(
        String login,
        String displayName,
        String twitchId,
        String profileImageUrl
) {

}
