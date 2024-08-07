package com.example.twitch.clip;

import com.example.twitch.auth.AuthenticationService;
import com.example.twitch.auth.TwitchUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class ClipService {

    private final ClipRepository clipRepository;



    @Value("${twitch-client-id}")
    private String twitchClientId;

    @Autowired
    public ClipService(ClipRepository clipRepository) {
        this.clipRepository = clipRepository;
    }

    public List<Clip> getClips() {
        return clipRepository.findAll();

    }

    public TwitchClipsResponse streamerPopularClips(String token, String login, String startedAt, String endedAt) {

        String twitchApiUrl = "https://api.twitch.tv/helix/clips";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Client-Id", twitchClientId);

        var twitchUserData = getTwitchUserInfoByLogin(token, login);

        var twitchUserId = twitchUserData.getData()[0].getId();


        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(twitchApiUrl)
                .queryParam("broadcaster_id", twitchUserId)
                .queryParam("started_at", startedAt)
                .queryParam("ended_at", endedAt);

        var restTemplate = new RestTemplate();
        ResponseEntity<TwitchClipsResponse> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TwitchClipsResponse.class
        );


        for(var clipData : response.getBody().getData()){

            var existingClip = clipRepository.findByClipId(clipData.getId());

            if(existingClip.isEmpty()) {
                Clip clip = new Clip(clipData.getId(), clipData.getUrl(), clipData.getEmbed_url(), clipData.getBroadcaster_id(), clipData.getBroadcaster_name()
                        , clipData.getCreator_id(), clipData.getCreator_name(), clipData.getVideo_id(), clipData.getGame_id(), clipData.getLanguage(), clipData.getTitle()
                        , clipData.getView_count(), clipData.getCreated_at(), clipData.getThumbnail_url(), clipData.getDuration(), clipData.getVod_offset());

                clipRepository.save(clip);
            } else {
                System.out.println("Clip with clipId " + clipData.getId() + " already exists.");
            }

        }

        return response.getBody();

    }

    public TwitchUsersResponse getTwitchUserInfoByLogin(String token, String login) {

        String twitchApiUrl = "https://api.twitch.tv/helix/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Client-Id", twitchClientId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(twitchApiUrl)
                .queryParam("login", login);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TwitchUsersResponse> twitchUserResponse = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TwitchUsersResponse.class
        );

        var twitchUserResponseData = twitchUserResponse.getBody();

        return twitchUserResponseData;

    }



}
