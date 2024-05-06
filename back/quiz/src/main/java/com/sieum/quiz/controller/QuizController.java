package com.sieum.quiz.controller;

import com.sieum.quiz.dto.request.QuizHistoryCreationRequest;
import com.sieum.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Save quiz history after user submitted the answer")
    @PostMapping("/result")
    public ResponseEntity<?> createQuizHistory(
            @RequestHeader("Authorization") final String authorization,
            @RequestBody final QuizHistoryCreationRequest quizHistoryCreationRequest) {

        final long userId = quizService.getCurrentUserId(authorization);
        quizService.createQuizHistory(userId, quizHistoryCreationRequest);

        return ResponseEntity.noContent().build();
    }
}
