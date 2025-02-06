package com.example.twitch.clip;

public record ClipDto(
        String clipId,
        String url,
        String embedUrl,
        String broadcasterId,
        String broadcasterName,
        String creatorId,
        String creatorName,
        String videoId,
        String gameId,
        String language,
        String clipTitle,
        Integer viewCount,
        String createdAt,
        String thumbnailUrl,
        Float clipDuration,
        Integer vodOffset,
        String streamerLogin,
        String streamerImageUrl
) {

}
