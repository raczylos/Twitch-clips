package com.example.twitch.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwitchUsersResponse {

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
        private String displayName;
        @JsonProperty("email")
        private String email;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;


        public String getId() {
            return id;
        }


        public String getLogin() {
            return login;
        }


        public String getDisplayName() {
            return displayName;
        }


        public String getEmail() {
            return email;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }
    }
}
