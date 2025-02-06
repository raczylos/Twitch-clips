package com.example.twitch.streamer;

import com.example.twitch.clip.Clip;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "streamer", fetch = FetchType.LAZY)
    private List<Clip> clips;

    public Streamer() {
    }

    public Streamer(Long id, String login, String displayName, String twitchId, String profileImageUrl, List<Clip> clips) {
        this.id = id;
        this.login = login;
        this.displayName = displayName;
        this.twitchId = twitchId;
        this.profileImageUrl = profileImageUrl;
        this.clips = clips;
    }

    public Streamer(String login, String displayName, String twitchId, String profileImageUrl, List<Clip> clips) {
        this.login = login;
        this.displayName = displayName;
        this.twitchId = twitchId;
        this.profileImageUrl = profileImageUrl;
        this.clips = clips;
    }

    public Long getId() {
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
