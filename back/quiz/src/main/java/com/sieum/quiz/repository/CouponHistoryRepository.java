package com.sieum.quiz.repository;

import com.sieum.quiz.domain.CouponHistory;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {
    boolean existsByCreatedAtAfter(LocalDateTime today);
}
