package com.example.twitch.clip;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TwitchClipsResponse {

    @JsonProperty("data")
    private ClipData[] data;

    public ClipData[] getData() {
        return data;
    }

    public static class ClipData {
        @JsonProperty("id")
        private String id;
        @JsonProperty("url")
        private String url;
        @JsonProperty("embed_url")
        private String embed_url;
        @JsonProperty("broadcaster_id")
        private String broadcaster_id;
        @JsonProperty("broadcaster_name")
        private String broadcaster_name;
        @JsonProperty("creator_id")
        private String creator_id;
        @JsonProperty("creator_name")
        private String creator_name;
        @JsonProperty("video_id")
        private String video_id;
        @JsonProperty("game_id")
        private String game_id;
        @JsonProperty("language")
        private String language;
        @JsonProperty("title")
        private String title;
        @JsonProperty("view_count")
        private int view_count;
        @JsonProperty("created_at")
        private String created_at;
        @JsonProperty("thumbnail_url")
        private String thumbnail_url;
        @JsonProperty("duration")
        private float duration;
        @JsonProperty("vod_offset")
        private int vod_offset;

        public String getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public String getEmbed_url() {
            return embed_url;
        }

        public String getBroadcaster_id() {
            return broadcaster_id;
        }

        public String getBroadcaster_name() {
            return broadcaster_name;
        }

        public String getCreator_id() {
            return creator_id;
        }

        public String getCreator_name() {
            return creator_name;
        }

        public String getVideo_id() {
            return video_id;
        }

        public String getGame_id() {
            return game_id;
        }

        public String getLanguage() {
            return language;
        }

        public String getTitle() {
            return title;
        }

        public int getView_count() {
            return view_count;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getThumbnail_url() {
            return thumbnail_url;
        }

        public float getDuration() {
            return duration;
        }

        public int getVod_offset() {
            return vod_offset;
        }

        @Override
        public String toString() {
            return "ClipData{" +
                    "id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    ", embed_url='" + embed_url + '\'' +
                    ", broadcaster_id='" + broadcaster_id + '\'' +
                    ", broadcaster_name='" + broadcaster_name + '\'' +
                    ", creator_id='" + creator_id + '\'' +
                    ", creator_name='" + creator_name + '\'' +
                    ", video_id='" + video_id + '\'' +
                    ", game_id='" + game_id + '\'' +
                    ", language='" + language + '\'' +
                    ", title='" + title + '\'' +
                    ", view_count=" + view_count +
                    ", created_at='" + created_at + '\'' +
                    ", thumbnail_url='" + thumbnail_url + '\'' +
                    ", duration=" + duration +
                    ", vod_offset=" + vod_offset +
                    '}';
        }
    }


}
