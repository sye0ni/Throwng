package com.sieum.quiz.service;

import static com.sieum.quiz.exception.CustomExceptionStatus.INVALID_QUIZ_ID;

import com.sieum.quiz.controller.feign.TokenAuthClient;
import com.sieum.quiz.domain.Quiz;
import com.sieum.quiz.domain.QuizHistory;
import com.sieum.quiz.domain.enums.CouponRoute;
import com.sieum.quiz.dto.request.QuizHistoryCreationRequest;
import com.sieum.quiz.dto.response.CouponIssuanceStatusResponse;
import com.sieum.quiz.exception.BadRequestException;
import com.sieum.quiz.repository.CouponReposistory;
import com.sieum.quiz.repository.QuizHistoryRepository;
import com.sieum.quiz.repository.QuizRepository;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final TokenAuthClient tokenAuthClient;
    private final CouponReposistory couponRepository;
    private final QuizRepository quizRepository;
    private final QuizHistoryRepository quizHistoryRepository;

    public List<CouponIssuanceStatusResponse> getCouponIssuanceStatus(final long userId) {

        return EnumSet.allOf(CouponRoute.class).stream()
                .map(
                        route -> {
                            boolean result =
                                    couponRepository.existsByCreatedAtAfterAndRouteAndUserId(
                                            LocalDate.now().atStartOfDay(),
                                            CouponRoute.findByName(String.valueOf(route)),
                                            userId);
                            return CouponIssuanceStatusResponse.builder()
                                    .status(result)
                                    .name(CouponRoute.findByName(String.valueOf(route)))
                                    .build();
                        })
                .collect(Collectors.toList());
    }

    public void createQuizHistory(
            final long userId, final QuizHistoryCreationRequest quizHistoryCreationRequest) {

        final Optional<Quiz> quiz = quizRepository.findById(quizHistoryCreationRequest.getQuizId());

        if (quiz.isEmpty()) {
            throw new BadRequestException(INVALID_QUIZ_ID);
        }

        quizHistoryRepository.save(
                QuizHistory.builder()
                        .quiz(quiz.get())
                        .submit(quizHistoryCreationRequest.getSubmit())
                        .result(quizHistoryCreationRequest.isResult())
                        .userId(userId)
                        .build());
    }

    public long getCurrentUserId(final String authorization) {
        return tokenAuthClient.getUserId(authorization);
    }
}
