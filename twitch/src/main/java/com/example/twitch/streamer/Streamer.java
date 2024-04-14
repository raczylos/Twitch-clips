package com.example.twitch.streamer;

import com.example.twitch.user.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "streamer")
public class Streamer {

    @Id
    @GeneratedValue
    private Long id;
    private String login;
    private String displayName;
    @Column(unique = true)
    private String twitchId;
    private String profileImageUrl;

    public Streamer() {
    }

    public Streamer(Long id, String login, String displayName, String twitchId, String profileImageUrl) {
        this.id = id;
        this.login = login;
        this.displayName = displayName;
        this.twitchId = twitchId;
        this.profileImageUrl = profileImageUrl;
    }

    public Streamer(String login, String displayName, String twitchId, String profileImageUrl) {
        this.login = login;
        this.displayName = displayName;
        this.twitchId = twitchId;
        this.profileImageUrl = profileImageUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTwitchId() {
        return twitchId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
