package com.sieum.quiz.service;

import static com.sieum.quiz.exception.CustomExceptionStatus.INVALID_QUIZ_ID;
import static com.sieum.quiz.exception.CustomExceptionStatus.NOT_TODAY_QUIZ_ID;

import com.sieum.quiz.controller.feign.TokenAuthClient;
import com.sieum.quiz.domain.Quiz;
import com.sieum.quiz.domain.QuizHistory;
import com.sieum.quiz.domain.enums.CouponRoute;
import com.sieum.quiz.domain.enums.QuizType;
import com.sieum.quiz.dto.request.QuizHistoryCreationRequest;
import com.sieum.quiz.dto.response.CouponIssuanceStatusResponse;
import com.sieum.quiz.dto.response.QuizResponse;
import com.sieum.quiz.exception.BadRequestException;
import com.sieum.quiz.repository.CouponReposistory;
import com.sieum.quiz.repository.QuizHistoryRepository;
import com.sieum.quiz.repository.QuizRepository;
import com.sieum.quiz.util.RedisUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final RedisUtil redisUtil;
    private final TokenAuthClient tokenAuthClient;
    private final CouponReposistory couponRepository;
    private final QuizRepository quizRepository;
    private final QuizHistoryRepository quizHistoryRepository;

    public List<CouponIssuanceStatusResponse> getCouponIssuanceStatus(final long userId) {

        return EnumSet.allOf(CouponRoute.class).stream()
                .map(
                        route -> {
                            boolean result;
                            if (route.getName().equals("quiz")) {
                                result =
                                        quizHistoryRepository.existsByCreatedAtAfterAndUserId(
                                                LocalDate.now().atStartOfDay(), userId);
                            } else {
                                result =
                                        couponRepository.existsByCreatedAtAfterAndRouteAndUserId(
                                                LocalDate.now().atStartOfDay(),
                                                CouponRoute.findByName(String.valueOf(route)),
                                                userId);
                            }
                            return CouponIssuanceStatusResponse.builder()
                                    .status(result)
                                    .name(route.getName())
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

        final String key =
                "quiz_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final List<QuizResponse> todayQuiz = (List<QuizResponse>) redisUtil.getObject(key);

        if (!todayQuiz.stream()
                .anyMatch(q -> q.getQuizId() == quizHistoryCreationRequest.getQuizId())) {
            throw new BadRequestException(NOT_TODAY_QUIZ_ID);
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

    public List<QuizResponse> getQuizList() {
        final String key =
                "quiz_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (redisUtil.getObject(key) != null) {
            return (List<QuizResponse>) redisUtil.getObject(key);
        }

        final List<QuizResponse> quizlist =
                quizRepository.findAll().stream()
                        .map(
                                quiz ->
                                        QuizResponse.builder()
                                                .quizId(quiz.getId())
                                                .question(quiz.getQuestion())
                                                .answer(quiz.getAnswer())
                                                .quizType(
                                                        QuizType.valueOf(quiz.getQuizType())
                                                                .getValue())
                                                .quizImage(quiz.getQuizImage())
                                                .previewUrl(quiz.getPreviewUrl())
                                                .choice(quiz.getChoice())
                                                .build())
                        .collect(Collectors.toList());

        final List<Integer> indexes = createRandomQuiz(quizlist.size());
        final List<QuizResponse> todayQuizList = new ArrayList<>();

        indexes.stream().forEach(index -> todayQuizList.add(quizlist.get(index)));

        redisUtil.setObject(key, todayQuizList);

        return todayQuizList;
    }

    private List<Integer> createRandomQuiz(final int size) {
        final Random random = new Random();

        final List<Integer> selectedNumbers = new ArrayList<>();
        while (selectedNumbers.size() < 3) {
            final int randomNumber = random.nextInt(size);
            if (!selectedNumbers.contains(randomNumber)) {
                selectedNumbers.add(randomNumber);
            }
        }

        return selectedNumbers;
    }
}
