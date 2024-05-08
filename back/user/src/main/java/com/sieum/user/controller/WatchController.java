package com.sieum.user.controller;

import com.sieum.user.service.WatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/watch")
@RequiredArgsConstructor
public class WatchController {

    private final WatchService watchService;

    @GetMapping("/otp")
    public ResponseEntity<?> getOTP(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok().body(watchService.createOTP(accessToken));
    }
}
