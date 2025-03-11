package com.example.twitch.user;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TwitchUserService {

    private final TwitchUserRepository twitchUserRepository;
    @Value("${twitch-client-id}")
    private String twitchClientId;


    @Autowired
    public TwitchUserService(TwitchUserRepository twitchUserRepository) {
        this.twitchUserRepository = twitchUserRepository;
    }


    public Optional<TwitchUser> getTwitchUserByLogin(String login) {
        return twitchUserRepository.findByLogin(login);
    }

    public Optional<TwitchUser> getTwitchUserByTwitchId(String twitchId) {
        return twitchUserRepository.findByTwitchId(twitchId);
    }

    public Optional<TwitchUser> getTwitchUserByUserId(Integer userId) {
        return twitchUserRepository.findById(userId);
    }

    public TwitchUsersResponse getTwitchUserInfoByLogin(String token, String login) {
        System.out.println("login " + login);
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

        return twitchUserResponse.getBody();

    }

    public TwitchUserFollowsResponse.FollowData[] getTwitchUserFollows(String twitchAccessToken, String login) {
        String twitchApiUrl = "https://api.twitch.tv/helix/channels/followed";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(twitchAccessToken);
        headers.set("Client-Id", twitchClientId);

        Optional<TwitchUser> twitchUser = getTwitchUserByLogin(login);
        if (twitchUser.isPresent()) {
            var twitchUserId = twitchUser.get().getTwitchId();
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(twitchApiUrl)
                    .queryParam("user_id", twitchUserId);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<TwitchUserFollowsResponse> twitchUserFollowsResponse = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    TwitchUserFollowsResponse.class
            );
            List<TwitchUserFollowsResponse.FollowData> allFollowData = new ArrayList<>(Arrays.asList(twitchUserFollowsResponse.getBody().getData()));
            while(twitchUserFollowsResponse.getBody().getPagination().getCursor() != null){
                builder.replaceQueryParam("after", twitchUserFollowsResponse.getBody().getPagination().getCursor());

                twitchUserFollowsResponse = restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        TwitchUserFollowsResponse.class
                );
                allFollowData.addAll(Arrays.asList(twitchUserFollowsResponse.getBody().getData()));
            }

            TwitchUserFollowsResponse.FollowData[] mergedArray = allFollowData.toArray(new TwitchUserFollowsResponse.FollowData[0]);

            return mergedArray;
        } else {
            System.out.println("User with that email is not in a database");
            return null;
        }

    }
}
