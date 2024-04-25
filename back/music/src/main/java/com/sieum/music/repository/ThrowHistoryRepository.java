package com.sieum.music.repository;

import com.sieum.music.domain.ThrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThrowHistoryRepository extends JpaRepository<ThrowHistory, Long> {

    boolean existsByUserIdAndThrowItemId(long userId, long throwId);
}
