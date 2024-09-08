package com.babelbeats.server.repository;

import com.babelbeats.server.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Long> {
    Optional<Music> findByYtbId(String ytbId);
}
