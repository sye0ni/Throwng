package com.sieum.user.controller;

import com.sieum.user.dto.request.OTPRequest;
import com.sieum.user.service.WatchService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/watch")
@RequiredArgsConstructor
public class WatchController {

    private final WatchService watchService;

    @GetMapping("/otp")
    public ResponseEntity<?> getOTP(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok().body(watchService.createOTP(accessToken));
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@Valid @RequestBody final OTPRequest otpRequest) {
        return ResponseEntity.ok().body(watchService.authenticate(otpRequest));
    }

    @GetMapping(value = "/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<String> send() {
        return Mono.just("테스트");
    }
}
