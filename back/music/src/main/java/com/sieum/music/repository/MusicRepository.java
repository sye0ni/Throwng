package com.sieum.music.repository;

import com.sieum.music.domain.ThrowItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<ThrowItem, Long> {}
