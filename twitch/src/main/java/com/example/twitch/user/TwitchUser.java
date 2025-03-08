package com.example.twitch.user;


import com.example.twitch.follower.Follower;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "TWITCH_USER")
public class TwitchUser extends AbstractUser implements UserDetails {


    @Column(name = "twitch_id", unique = true, nullable = false)
    private String twitchId;

    @OneToMany(mappedBy = "twitchUser", fetch = FetchType.LAZY)
    private List<Follower> followingStreamers = new ArrayList<>();


    public TwitchUser() {

    }

    public TwitchUser(Integer id, String login, String email, String twitchId, Role role, UserType userType) {
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

    public void setTwitchId(String twitchId) {
        this.twitchId = twitchId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole().name()));
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

    public List<Follower> getFollowingStreamers() {
        return followingStreamers;
    }

    public void setFollowingStreamers(List<Follower> followingStreamers) {
        this.followingStreamers = followingStreamers;
    }
}
