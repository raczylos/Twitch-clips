package com.example.twitch.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TwitchUserRepository extends JpaRepository<TwitchUser, Integer> {
    Optional<TwitchUser> findByEmail(String email);

    Optional<TwitchUser> findByLogin(String login);

    Optional<TwitchUser> findByTwitchId(String twitchId);

    Optional<TwitchUser> findById(Long userId);

}
