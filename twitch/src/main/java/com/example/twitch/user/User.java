package com.example.twitch.user;

import com.example.twitch.token.Token;
import jakarta.persistence.*;

import java.util.List;

//@Entity
//@Table(name = "_user")

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "_user")
public abstract class User {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String login;

    private UserType userType;
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Enumerated(EnumType.STRING)
    private Role role;

    protected User() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
