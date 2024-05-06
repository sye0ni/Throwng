package com.sieum.quiz.repository;

import com.sieum.quiz.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponReposistory extends JpaRepository<Coupon, Long> {}
