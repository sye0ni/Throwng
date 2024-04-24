package com.sieum.music.repository;

import com.sieum.music.domain.Throw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Throw, Long> {}
