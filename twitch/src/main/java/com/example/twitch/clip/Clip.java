package com.example.twitch.clip;

import jakarta.persistence.*;

import java.time.LocalDateTime;


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
    private String clipId;
    private String userName;
    private String creatorName;
    private String broadcasterName;
    private String clipTitle;
    private Integer clipDuration;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    public Clip () {}

    public Clip(Long id, String clipId, String userName, String creatorName, String broadcasterName, String clipTitle, Integer clipDuration, String thumbnailUrl, LocalDateTime createdAt) {
        this.id = id;
        this.clipId = clipId;
        this.userName = userName;
        this.creatorName = creatorName;
        this.broadcasterName = broadcasterName;
        this.clipTitle = clipTitle;
        this.clipDuration = clipDuration;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }

    public Clip(String clipId, String userName, String creatorName, String broadcasterName, String clipTitle, Integer clipDuration, String thumbnailUrl, LocalDateTime createdAt) {
        this.clipId = clipId;
        this.userName = userName;
        this.creatorName = creatorName;
        this.broadcasterName = broadcasterName;
        this.clipTitle = clipTitle;
        this.clipDuration = clipDuration;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClipId() {
        return clipId;
    }

    public void setClipId(String clipId) {
        this.clipId = clipId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getBroadcasterName() {
        return broadcasterName;
    }

    public void setBroadcasterName(String broadcasterName) {
        this.broadcasterName = broadcasterName;
    }

    public String getClipTitle() {
        return clipTitle;
    }

    public void setClipTitle(String clipTitle) {
        this.clipTitle = clipTitle;
    }

    public Integer getClipDuration() {
        return clipDuration;
    }

    public void setClipDuration(Integer clipDuration) {
        this.clipDuration = clipDuration;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
