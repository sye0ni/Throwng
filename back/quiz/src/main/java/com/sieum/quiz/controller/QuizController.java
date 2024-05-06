package com.sieum.quiz.controller;

import com.sieum.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "Return whether or not to issue a coupon for the day")
    @GetMapping("/contents")
    public ResponseEntity<?> getCouponIssuanceStatus(
            @RequestHeader("Authorization") final String authorization) {
        final long userId = quizService.getCurrentUserId(authorization);
        return ResponseEntity.ok().body(quizService.getCouponIssuanceStatus(userId));
    }
}
