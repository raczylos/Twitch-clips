package com.example.twitch.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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
        private String broadcaster_id;
        @JsonProperty("broadcaster_login")
        private String broadcaster_login;
        @JsonProperty("broadcaster_name")
        private String broadcaster_name;
        @JsonProperty("followed_at")
        private String followed_at;

        public String getBroadcaster_id() {
            return broadcaster_id;
        }


        public String getBroadcaster_login() {
            return broadcaster_login;
        }



        public String getBroadcaster_name() {
            return broadcaster_name;
        }


        public String getFollowed_at() {
            return followed_at;
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
