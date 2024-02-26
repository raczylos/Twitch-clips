package com.example.twitch.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwitchUserResponse {

    @JsonProperty("data")
    private TwitchUserData[] data;

    public TwitchUserData[] getData() {
        return data;
    }

    public static class TwitchUserData {
        @JsonProperty("id")
        private String id;
        @JsonProperty("login")
        private String login;
        @JsonProperty("display_name")
        private String display_name;
        @JsonProperty("email")
        private String email;


        public String getId() {
            return id;
        }


        public String getLogin() {
            return login;
        }


        public String getDisplayName() {
            return display_name;
        }


        public String getEmail() {
            return email;
        }
    }
}
