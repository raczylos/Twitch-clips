package com.example.twitch.clip;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClipRepository extends JpaRepository<Clip, Integer> {

    Optional<Clip> findByTwitchClipId(String clipId);

    List<Clip> findClipsByOrderByViewCountDesc();

    List<Clip> findClipsByOrderByViewCountDesc(Pageable pageable);

    List<Clip> findClipsByBroadcasterIdAndViewCountGreaterThanAndCreatedAtBetweenOrderByViewCountDesc(String StreamerId, Integer viewCount, String startedAt, String endedAt);

    List<Clip> findClipsByViewCountGreaterThanAndCreatedAtBetweenOrderByViewCountDesc(Integer viewCount, String startedAt, String endedAt);
}
