package com.example.twitch.follower;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Integer> {

    Optional<Follower> findByStreamerIdAndTwitchUserId(Integer streamerId, Integer userId);

    List<Follower> findFollowersByTwitchUserId(Integer userId);

}

