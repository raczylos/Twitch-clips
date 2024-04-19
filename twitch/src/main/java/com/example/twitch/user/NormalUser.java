package com.example.twitch.user;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "normal_user")
public class NormalUser extends User implements UserDetails {



    @Column
    private String password;


    public NormalUser() {

    }

    public NormalUser(Long id, String login, String email, String password, Role role, UserType userType) {
        setId(id);
        setLogin(login);
        setEmail(email);
        this.password = password;
        setRole(role);
        setUserType(userType);

    }

    public NormalUser(String login, String email, String password, Role role, UserType userType) {
        setLogin(login);
        setEmail(email);
        this.password = password;
        setRole(role);
        setUserType(userType);
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole().name()));
    }



    @Override
    public String getPassword() {
        return password;
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
