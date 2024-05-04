package com.sieum.notification.controller;

import com.sieum.notification.dto.request.NotificationRequest;
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

    @Operation(summary = "Mongodb insertion test")
    @PostMapping("/mongo/insert")
    public ResponseEntity<?> createDocument(
            @RequestBody final NotificationRequest notificationRequest) {
        notificationService.mongoInsertTest(notificationRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mongodb search test")
    @GetMapping("/mongo/search/{notificationName}")
    public ResponseEntity<?> getDocument(@PathVariable final String notificationName) {
        return ResponseEntity.ok().body(notificationService.mongoSearchTest(notificationName));
    }
}
