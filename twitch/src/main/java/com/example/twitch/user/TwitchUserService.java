package com.example.twitch.user;

import com.example.twitch.auth.AuthenticationService;
import com.example.twitch.auth.TwitchUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class TwitchUserService {

    @Value("${twitch-client-id}")
    private String twitchClientId;

    private final TwitchUserRepository twitchUserRepository;
    private final AuthenticationService authenticationService;


    @Autowired
    public TwitchUserService(TwitchUserRepository twitchUserRepository, AuthenticationService authenticationService) {
        this.twitchUserRepository = twitchUserRepository;
        this.authenticationService = authenticationService;
    }



    public Optional<TwitchUser> getTwitchUserByLogin(String login) {
        return twitchUserRepository.findByLogin(login);
    }

    public Optional<TwitchUser> getTwitchUserByTwitchId(String twitchId) {
        return twitchUserRepository.findByTwitchId(twitchId);
    }

    public Optional<TwitchUser> getTwitchUserByUserId(Long userId) {
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

        var twitchUserResponseData = twitchUserResponse.getBody();

        return twitchUserResponseData;

    }

    public TwitchUserFollowsResponse getTwitchUserFollows(String twitchAccessToken, String twitchRefreshToken, String login) {
        var response = authenticationService.validateTwitchAccessToken(twitchAccessToken);

//        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
//            var refreshResponse = authenticationService.refreshTwitchAccessToken(twitchRefreshToken);
//            twitchAccessToken = refreshResponse.getBody().getAccessToken();
//            twitchRefreshToken = refreshResponse.getBody().getRefreshToken();
//        }

        String twitchApiUrl = "https://api.twitch.tv/helix/channels/followed";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(twitchAccessToken);
        headers.set("Client-Id", twitchClientId);

        Optional<TwitchUser> twitchUser = getTwitchUserByLogin(login);
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
