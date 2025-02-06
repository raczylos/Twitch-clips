package com.example.twitch.user;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TwitchUserFollowsResponse {
    @JsonProperty("total")
    private int total;

    @JsonProperty("data")
    private FollowData[] data;

    @JsonProperty("pagination")
    private Pagination pagination;


    public int getTotal() {
        return total;
    }


    public FollowData[] getData() {
        return data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public static class FollowData {
        @JsonProperty("broadcaster_id")
        private String streamerId;
        @JsonProperty("broadcaster_login")
        private String streamerLogin;
        @JsonProperty("broadcaster_name")
        private String streamerName;
        @JsonProperty("followed_at")
        private String followedAt;

        public String getStreamerId() {
            return streamerId;
        }

        public String getStreamerLogin() {
            return streamerLogin;
        }

        public String getStreamerName() {
            return streamerName;
        }

        public String getFollowedAt() {
            return followedAt;
        }
    }

    public static class Pagination {
        @JsonProperty("cursor")
        private String cursor;


        public String getCursor() {
            return cursor;
        }
    }
}

