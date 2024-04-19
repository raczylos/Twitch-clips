package com.example.twitch.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<NormalUser, Integer> {
    Optional<NormalUser> findByEmail(String email);

}



