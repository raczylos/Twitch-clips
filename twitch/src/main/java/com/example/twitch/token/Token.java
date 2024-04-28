package com.example.twitch.token;

import com.example.twitch.user.User;
import com.example.twitch.user.UserType;
import jakarta.persistence.*;

@Entity
public class Token {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType  tokenType;

    @Enumerated(EnumType.STRING)
    private Type  type;


    private boolean expired;

    private boolean revoked;



    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Token() {
    }

    public Token(String token, TokenType tokenType, boolean expired, boolean revoked, User user, Type type) {
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
