package com.sieum.quiz.controller;

import com.sieum.quiz.dto.request.QuizExperienceCountRequest;
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

    @GetMapping("/list")
    public ResponseEntity<?> getQuizList(
            @RequestHeader("Authorization") final String authorization) {
        return ResponseEntity.ok().body(quizService.getQuizList());
    }

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
        return ResponseEntity.ok()
                .body(quizService.createQuizHistory(userId, quizHistoryCreationRequest));
    }

    @Operation(summary = "Feign Client - Number of quizzes experienced by the user")
    @PostMapping("/content-experience")
    public ResponseEntity<?> getQuizExperienceCount(
            @RequestBody final QuizExperienceCountRequest quizExperienceCountRequest) {

        return ResponseEntity.ok()
                .body(quizService.getQuizExperienceCount(quizExperienceCountRequest));
    }
}
