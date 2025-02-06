package com.example.twitch.user;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "twitch_user")
public class TwitchUser extends User implements UserDetails {



    @Column(unique = true)
    private String twitchId;


    public TwitchUser() {

    }
    public TwitchUser(Long id, String login, String email, String twitchId, Role role, UserType userType) {
        setId(id);
        setLogin(login);
        setEmail(email);
        this.twitchId = twitchId;
        setRole(role);
        setUserType(userType);
    }

    public TwitchUser(String login, String email, String twitchId, Role role, UserType userType) {
        setLogin(login);
        setEmail(email);
        this.twitchId = twitchId;
        setRole(role);
        setUserType(userType);
    }

    public String getTwitchId() {
        return twitchId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole().name()));
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
        return getEmail();
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
