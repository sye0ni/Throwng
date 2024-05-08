package com.sieum.quiz.repository;

import com.sieum.quiz.domain.Coupon;
import com.sieum.quiz.domain.CouponHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {

    CouponHistory findTopByCouponIdOrderByCreatedAtDesc(final long CouponId);

}
