package com.example.twitch.user;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "twitch_user")
public class TwitchUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    private String login;
    private String email;

    @Column(unique = true)
    private String twitchId;
    @Enumerated(EnumType.STRING)
    private Role role;

    public TwitchUser() {

    }
    public TwitchUser(Long id, String login, String email, String twitchId, Role role) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.twitchId = twitchId;
        this.role = role;
    }

    public TwitchUser(String login, String email, String twitchId, Role role) {
        this.login = login;
        this.email = email;
        this.twitchId = twitchId;
        this.role = role;
    }

    public String getTwitchId() {
        return twitchId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTwitchId(String twitchId) {
        this.twitchId = twitchId;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
