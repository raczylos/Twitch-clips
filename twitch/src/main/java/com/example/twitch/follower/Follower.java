package com.example.twitch.follower;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table
public class Follower {
    @Id
    @SequenceGenerator(
            name = "follower_sequence",
            sequenceName = "follower_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "follower_sequence"
    )
    private Long id;

    private Long userId;

    private Long streamerId;

    private String streamerLogin;

    public Follower() {

    }
    public Follower(Long id, Long userId, Long streamerId, String streamerLogin) {
        this.id = id;
        this.userId = userId;
        this.streamerId = streamerId;
        this.streamerLogin = streamerLogin;
    }

    public Follower(Long userId, Long streamerId, String streamerLogin) {
        this.userId = userId;
        this.streamerId = streamerId;
        this.streamerLogin = streamerLogin;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(Long streamerId) {
        this.streamerId = streamerId;
    }

    public String getStreamerLogin() {
        return streamerLogin;
    }

    public void setStreamerLogin(String streamerLogin) {
        this.streamerLogin = streamerLogin;
    }
}
