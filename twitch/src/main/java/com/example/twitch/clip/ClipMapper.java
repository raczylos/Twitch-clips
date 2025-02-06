package com.example.twitch.clip;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ClipMapper {

    @Mapping(target = "streamerLogin", source = "streamer.login")
    @Mapping(target = "streamerImageUrl", source = "streamer.profileImageUrl")
    ClipDto entityToClipDto(Clip clip);

    Clip clipDtoToEntity(ClipDto clipDto);
}