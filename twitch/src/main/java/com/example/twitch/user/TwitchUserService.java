package com.example.twitch.user;

import com.example.twitch.auth.TwitchUsersResponse;
import com.example.twitch.clip.Clip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class TwitchUserService {

    @Value("${twitch-client-id}")
    private String twitchClientId;

    private final TwitchUserRepository twitchUserRepository;


    @Autowired
    public TwitchUserService(TwitchUserRepository twitchUserRepository) {
        this.twitchUserRepository = twitchUserRepository;
    }

    public Optional<TwitchUser> getTwitchUser(String email) {
        return twitchUserRepository.findByEmail(email);

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

    public TwitchUserFollowsResponse getTwitchUserFollows(String token, String email) {

        String twitchApiUrl = "https://api.twitch.tv/helix/channels/followed";


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Client-Id", twitchClientId);

        Optional<TwitchUser> twitchUser = getTwitchUser(email);

        if(twitchUser.isPresent()) {

            var twitchUserId =  twitchUser.get().getTwitchId();

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(twitchApiUrl)
                    .queryParam("user_id", twitchUserId);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<TwitchUserFollowsResponse> twitchUserFollowsResponse = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    TwitchUserFollowsResponse.class
            );

            return twitchUserFollowsResponse.getBody();
        } else {
            System.out.println("User with that email is not in a database");
            return null;
        }

    }
}
