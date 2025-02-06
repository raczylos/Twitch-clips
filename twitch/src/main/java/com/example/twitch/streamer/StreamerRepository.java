package com.example.twitch.streamer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StreamerRepository extends JpaRepository<Streamer, Integer> {
    Optional<Streamer> findByLogin(String login);
    Optional<Streamer> findByTwitchId(String streamerId);
    Optional<Streamer> findById(Long id);

}
