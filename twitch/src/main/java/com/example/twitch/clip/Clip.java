package com.example.twitch.clip;

import com.example.twitch.streamer.Streamer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;


@Entity
@Table(name = "CLIP")
public class Clip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "twitch_clip_id", unique = true, nullable = false)
    private String twitchClipId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "embed_url", nullable = false)
    private String embedUrl;
    @Column(name = "broadcaster_id", nullable = false)
    private String broadcasterId;
    @Column(name = "broadcaster_name", nullable = false)
    private String broadcasterName;
    @Column(name = "creator_id", nullable = false)
    private String creatorId;
    @Column(name = "creator_name", nullable = false)
    private String creatorName;

    private String videoId;
    private String gameId;
    private String language;

    @Column(name = "clip_title", nullable = false)
    private String clipTitle;
    @Column(name = "view_count", nullable = false)
    private Integer viewCount;
    private String createdAt;
    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;
    @Column(name = "clip_duration", nullable = false)
    private Float clipDuration;
    private Integer vodOffset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "streamer_id", nullable = false)
    private Streamer streamer;

    public Clip() {
    }

    public Clip(Integer id, String twitchClipId, String url, String embedUrl, String broadcasterId, String broadcasterName, String creatorId, String creatorName, String videoId, String gameId, String language, String clipTitle, Integer viewCount, String createdAt, String thumbnailUrl, Float clipDuration, Integer vodOffset, Streamer streamer) {
        this.id = id;
        this.twitchClipId = twitchClipId;
        this.url = url;
        this.embedUrl = embedUrl;
        this.broadcasterId = broadcasterId;
        this.broadcasterName = broadcasterName;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.videoId = videoId;
        this.gameId = gameId;
        this.language = language;
        this.clipTitle = clipTitle;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.thumbnailUrl = thumbnailUrl;
        this.clipDuration = clipDuration;
        this.vodOffset = vodOffset;
        this.streamer = streamer;
    }

    public Clip(String twitchClipId, String url, String embedUrl, String broadcasterId, String broadcasterName, String creatorId, String creatorName, String videoId, String gameId, String language, String clipTitle, Integer viewCount, String createdAt, String thumbnailUrl, Float clipDuration, Integer vodOffset, Streamer streamer) {
        this.twitchClipId = twitchClipId;
        this.url = url;
        this.embedUrl = embedUrl;
        this.broadcasterId = broadcasterId;
        this.broadcasterName = broadcasterName;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.videoId = videoId;
        this.gameId = gameId;
        this.language = language;
        this.clipTitle = clipTitle;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.thumbnailUrl = thumbnailUrl;
        this.clipDuration = clipDuration;
        this.vodOffset = vodOffset;
        this.streamer = streamer;
    }

    public Integer getId() {
        return id;
    }

    public String getTwitchClipId() {
        return twitchClipId;
    }

    public String getUrl() {
        return url;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public String getBroadcasterId() {
        return broadcasterId;
    }

    public String getBroadcasterName() {
        return broadcasterName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getLanguage() {
        return language;
    }

    public String getClipTitle() {
        return clipTitle;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Float getClipDuration() {
        return clipDuration;
    }

    public Integer getVodOffset() {
        return vodOffset;
    }

    public Streamer getStreamer() {
        return streamer;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Clip clip = (Clip) o;
        return Objects.equals(id, clip.id) && Objects.equals(twitchClipId, clip.twitchClipId) && Objects.equals(url, clip.url) && Objects.equals(embedUrl, clip.embedUrl) && Objects.equals(broadcasterId, clip.broadcasterId) && Objects.equals(broadcasterName, clip.broadcasterName) && Objects.equals(creatorId, clip.creatorId) && Objects.equals(creatorName, clip.creatorName) && Objects.equals(videoId, clip.videoId) && Objects.equals(gameId, clip.gameId) && Objects.equals(language, clip.language) && Objects.equals(clipTitle, clip.clipTitle) && Objects.equals(viewCount, clip.viewCount) && Objects.equals(createdAt, clip.createdAt) && Objects.equals(thumbnailUrl, clip.thumbnailUrl) && Objects.equals(clipDuration, clip.clipDuration) && Objects.equals(vodOffset, clip.vodOffset) && Objects.equals(streamer, clip.streamer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, twitchClipId, url, embedUrl, broadcasterId, broadcasterName, creatorId, creatorName, videoId, gameId, language, clipTitle, viewCount, createdAt, thumbnailUrl, clipDuration, vodOffset, streamer);
    }
}

