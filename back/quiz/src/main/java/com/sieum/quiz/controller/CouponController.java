package com.sieum.quiz.controller;

import com.sieum.quiz.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/coupons")
@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "Create a coupon")
    @GetMapping
    public ResponseEntity<?> createCoupon(
            @RequestHeader("Authorization") final String authorization) {
        final long userId = couponService.getCurrentUserId(authorization);
        return ResponseEntity.ok().body(couponService.createCoupon(userId));
    }
}
