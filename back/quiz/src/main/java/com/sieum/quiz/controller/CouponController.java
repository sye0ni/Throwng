package com.sieum.quiz.controller;

import com.sieum.quiz.dto.request.CouponValidationRequest;
import com.sieum.quiz.service.CouponService;
import com.sieum.quiz.validator.CouponValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/coupons")
@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final CouponValidator couponValidator;

    @Operation(summary = "Create a coupon")
    @GetMapping("/{route}")
    public ResponseEntity<?> createCoupon(
            @RequestHeader("Authorization") final String authorization,
            @PathVariable final String route) {
        final long userId = couponService.getCurrentUserId(authorization);
        return ResponseEntity.ok().body(couponService.createCoupon(userId, route));
    }

    @Operation(summary = "feign client")
    @GetMapping("/{userId}/history")
    public ResponseEntity<?> getCouponHistory(@PathVariable final long userId) {
        return ResponseEntity.ok().body(couponService.getCouponHistory(userId));
    }

    @Operation(summary = "feign client")
    @GetMapping("/validation")
    public ResponseEntity<?> getCouponHistory(
            @RequestBody final CouponValidationRequest couponValidationRequest) {
        couponValidator.validateCoupon(couponValidationRequest);
        return ResponseEntity.noContent().build();
    }
}
