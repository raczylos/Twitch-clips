package com.example.twitch.clip;

import com.example.twitch.follower.FollowerService;
import com.example.twitch.streamer.StreamerList;
import com.example.twitch.streamer.StreamerRepository;
import com.example.twitch.streamer.StreamerService;
import com.example.twitch.user.TwitchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClipService {

    private final ClipRepository clipRepository;

    private final TwitchUserService twitchUserService;

    private final StreamerService streamerService;

    private final FollowerService followerService;

    private final StreamerRepository streamerRepository;

    private final ClipMapper clipMapper;

    @Value("${twitch-client-id}")
    private String twitchClientId;

    @Autowired
    public ClipService(ClipRepository clipRepository, TwitchUserService twitchUserService, StreamerService streamerService, FollowerService followerService, StreamerRepository streamerRepository, ClipMapper clipMapper) {
        this.clipRepository = clipRepository;
        this.twitchUserService = twitchUserService;
        this.streamerService = streamerService;
        this.followerService = followerService;
        this.streamerRepository = streamerRepository;
        this.clipMapper = clipMapper;
    }

    public Clip getClip(String clipId) {
        var clip = clipRepository.findByClipId(clipId);
        return clip.orElse(null);
    }


    public List<Clip> getAllClips() {
        return clipRepository.findClipsByOrderByViewCountDesc();

    }

    public List<Clip> getClipsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return clipRepository.findClipsByOrderByViewCountDesc(pageable);
    }

    public List<Clip> getPopularClipsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return clipRepository.findClipsByOrderByViewCountDesc(pageable);
    }



    public List<ClipDto> streamerPopularClips(String token, String streamerId, String startedAt, String endedAt, Integer viewCount) {

        String twitchApiUrl = "https://api.twitch.tv/helix/clips";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Client-Id", twitchClientId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(twitchApiUrl)
                .queryParam("broadcaster_id", streamerId)
                .queryParam("started_at", startedAt)
                .queryParam("ended_at", endedAt);

        var restTemplate = new RestTemplate();
        ResponseEntity<TwitchClipsResponse> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TwitchClipsResponse.class
        );

        List<ClipDto> clips = new ArrayList<>();

        var streamer = streamerService.getStreamerByStreamerId(streamerId);

        for(var clipData : response.getBody().getData()){

            var existingClip = clipRepository.findByClipId(clipData.getId());

            if(existingClip.isEmpty()) {
                if(clipData.getView_count() < viewCount) {
                    System.out.println("Clip " + clipData.getId() + " rejected for too few views " + clipData.getView_count());
                } else {
//                    CHANGE - PASS OBJECT TO CONSTRUCTOR RATHER THAN THAT FROM BELOW
                    Clip clip = new Clip(clipData.getId(), clipData.getUrl(), clipData.getEmbed_url(),
                            clipData.getBroadcaster_id(), clipData.getBroadcaster_name(), clipData.getCreator_id(),
                            clipData.getCreator_name(), clipData.getVideo_id(), clipData.getGame_id(), clipData.getLanguage(),
                            clipData.getTitle(), clipData.getView_count(), clipData.getCreated_at(), clipData.getThumbnail_url(),
                            clipData.getDuration(), clipData.getVod_offset(), streamer);

                    clipRepository.save(clip);
                    clips.add(clipMapper.entityToClipDto(clip));
                }
            } else {
                System.out.println("Clip " + clipData.getId() + " already exists");
            }

        }

        return clips;

    }

    public List<ClipDto> addFollowedStreamersClips(String token, String login, String startedAt, String endedAt) {

        var userId = twitchUserService.getTwitchUserByLogin(login).get().getId();
        var follows = followerService.getFollowers(userId);

        List<ClipDto> clips = new ArrayList<>();

        if(follows == null){
            System.out.println("User doesnt have follows");
            return null;
        }

        for(var follow: follows) {
            var streamer = streamerService.getStreamer(follow.toString());
            if(streamer == null) {
                continue;
            }
            var streamerClips = streamerPopularClips(token, streamer.getTwitchId(), startedAt, endedAt, 200);
            clips.addAll(streamerClips);
        }

        System.out.println(clips);
        return clips;
    }

    public Page<Clip> getPopularStreamersClips(String startedAt, String endedAt, int page, int size) {

        List<Clip> clips = new ArrayList<>();

        for(var streamer: StreamerList.values()) {
            System.out.println(streamer.toString());
            var streamerId = streamerService.getStreamer(streamer.toString()).getTwitchId();
            var streamerClips = clipRepository.findClipsByBroadcasterIdAndViewCountGreaterThanAndCreatedAtBetweenOrderByViewCountDesc(streamerId, 100, startedAt, endedAt);


            clips.addAll(streamerClips);
        }

        Pageable pageable = PageRequest.of(page, size);

        int totalClips = clips.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalClips);

        List<Clip> pagedClips = clips.subList(start, end);

        return new PageImpl<>(pagedClips, pageable, totalClips);
    }

    public Page<ClipDto> getPopularClips(String startedAt, String endedAt, int page, int size) {

        var streamerClips = clipRepository.findClipsByViewCountGreaterThanAndCreatedAtBetweenOrderByViewCountDesc(100, startedAt, endedAt);

        Pageable pageable = PageRequest.of(page, size);

        int totalClips = streamerClips.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalClips);

        List<ClipDto> clipsDto = streamerClips.stream().map(clipMapper::entityToClipDto).collect(Collectors.toList());
        List<ClipDto> pagedClips = clipsDto.subList(start, end);

        return new PageImpl<>(pagedClips, pageable, totalClips);
    }

    public List<ClipDto> addPopularStreamersClipsBySystem(String token, String startedAt, String endedAt) {

        List<ClipDto> clips = new ArrayList<>();

        for(var streamerLogin: StreamerList.values()) {
            System.out.println(streamerLogin.toString());
            var streamer = streamerService.getStreamer(streamerLogin.toString());
            if(streamer == null) {
                continue;
            }

            var streamerClips = streamerPopularClips(token, streamer.getTwitchId(), startedAt, endedAt, 500);

            clips.addAll(streamerClips);
        }

        return clips;
    }


}
