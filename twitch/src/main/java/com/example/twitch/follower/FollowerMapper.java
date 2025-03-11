package com.example.twitch.follower;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface FollowerMapper {

    @Mapping(target = "twitchUserLogin", source = "twitchUser.login")
    @Mapping(target = "streamerDisplayName", source = "streamer.displayName")
    @Mapping(target = "twitchUserId", source = "twitchUser.id")
    @Mapping(target = "streamerId", source = "streamer.id")
    FollowerDto entityToFollowerDto(Follower follower);

//    Follower followerDtoToEntity(FollowerDto followerDto);
}
