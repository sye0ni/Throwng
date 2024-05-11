package com.sieum.notification.controller;

import com.sieum.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Mongodb search api - for test")
    @GetMapping("/mongo/search/{notificationName}")
    public ResponseEntity<?> getDocument(@PathVariable final String notificationName) {
        return ResponseEntity.ok().body(notificationService.mongoSearchTest(notificationName));
    }

    @Operation(summary = "Sending coupon expiration notification - for test")
    @PostMapping("/coupons")
    public ResponseEntity<?> createCouponExpirationNotification() {
        notificationService.createCouponExpirationNotification();
        return ResponseEntity.noContent().build();
    }
}
