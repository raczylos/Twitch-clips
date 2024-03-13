package com.example.twitch.clip;

import jakarta.persistence.*;


@Entity
@Table
public class Clip {
    @Id
    @SequenceGenerator(
            name = "clip_sequence",
            sequenceName = "clip_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "clip_sequence"
    )
    private Long id;
    @Column(unique = true)
    private String clipId;

    private String url;

    private String embedUrl;
    private String broadcasterId;
    private String broadcasterName;
    private String creatorId;
    private String creatorName;

    private String videoId;
    private String gameId;
    private String language;

    private String clipTitle;
    private Integer viewCount;
    private String createdAt;
    private String thumbnailUrl;
    private Float clipDuration;
    private Integer vodOffset;


    public Clip () {}

    public Clip(Long id, String clipId, String url, String embedUrl, String broadcasterId, String broadcasterName, String creatorId, String creatorName, String videoId, String gameId, String language, String clipTitle, Integer viewCount, String createdAt, String thumbnailUrl, Float clipDuration, Integer vodOffset) {
        this.id = id;
        this.clipId = clipId;
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
    }

    public Clip(String clipId, String url, String embedUrl, String broadcasterId, String broadcasterName, String creatorId, String creatorName, String videoId, String gameId, String language, String clipTitle, Integer viewCount, String createdAt, String thumbnailUrl, Float clipDuration, Integer vodOffset) {
        this.clipId = clipId;
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
    }

    public Long getId() {
        return id;
    }

    public String getClipId() {
        return clipId;
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
}
