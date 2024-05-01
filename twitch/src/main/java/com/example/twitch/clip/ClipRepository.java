package com.example.twitch.clip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClipRepository extends JpaRepository<Clip, Long> {
    Optional<Clip> findByClipId(String clipId);

    List<Clip> findClipsByOrderByViewCountDesc();

    List<Clip> findClipsByOrderByViewCountDesc(Pageable pageable);
}
