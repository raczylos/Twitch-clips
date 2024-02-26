package com.example.twitch.auth;

public class RegisterRequest {

    private String username;

    private String email;

    private String password;



    public RegisterRequest(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
