package com.example.twitch.streamer;

import com.example.twitch.clip.Clip;
import com.example.twitch.follower.Follower;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "STREAMER")
public class Streamer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "login", unique = true, nullable = false)
    private String login;
    @Column(name = "display_name", unique = true, nullable = false)
    private String displayName;
    @Column(name = "twitch_id", unique = true, nullable = false)
    private String twitchId;
    private String profileImageUrl;

    @OneToMany(mappedBy = "streamer", fetch = FetchType.LAZY)
    private List<Clip> clips = new ArrayList<>();

    @OneToMany(mappedBy = "streamer", fetch = FetchType.LAZY)
    private List<Follower> followers = new ArrayList<>();

    public Streamer() {
    }

    public Streamer(Integer id, String login, String displayName, String twitchId, String profileImageUrl) {
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

    public Integer getId() {
        return id;
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

    public List<Clip> getClips() {
        return clips;
    }

    public void setClips(List<Clip> clips) {
        this.clips = clips;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Streamer streamer = (Streamer) o;
        return Objects.equals(id, streamer.id) && Objects.equals(login, streamer.login) && Objects.equals(displayName, streamer.displayName) && Objects.equals(twitchId, streamer.twitchId) && Objects.equals(profileImageUrl, streamer.profileImageUrl) && Objects.equals(clips, streamer.clips);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, displayName, twitchId, profileImageUrl, clips);
    }
}
