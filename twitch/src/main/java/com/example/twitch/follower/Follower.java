package com.example.twitch.follower;

import com.example.twitch.streamer.Streamer;
import com.example.twitch.user.TwitchUser;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "FOLLOWER")
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "twitch_user_id", nullable = false)
    private TwitchUser twitchUser;

    @ManyToOne
    @JoinColumn(name = "streamer_id", nullable = false)
    private Streamer streamer;


    public Follower() {

    }

    public Follower(Integer id, TwitchUser twitchUser, Streamer streamer) {
        this.id = id;
        this.twitchUser = twitchUser;
        this.streamer = streamer;
    }

    public Follower(TwitchUser twitchUser, Streamer streamer) {
        this.twitchUser = twitchUser;
        this.streamer = streamer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TwitchUser getTwitchUser() {
        return twitchUser;
    }

    public void setTwitchUser(TwitchUser twitchUser) {
        this.twitchUser = twitchUser;
    }

    public Streamer getStreamer() {
        return streamer;
    }

    public void setStreamer(Streamer streamer) {
        this.streamer = streamer;
    }
}
