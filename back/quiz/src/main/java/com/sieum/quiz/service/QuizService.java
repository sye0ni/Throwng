package com.sieum.quiz.service;

import com.sieum.quiz.controller.feign.TokenAuthClient;
import com.sieum.quiz.domain.enums.CouponRoute;
import com.sieum.quiz.dto.response.CouponIssuanceStatusResponse;
import com.sieum.quiz.repository.CouponReposistory;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final TokenAuthClient tokenAuthClient;
    private final CouponReposistory couponRepository;

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

    public long getCurrentUserId(final String authorization) {
        return tokenAuthClient.getUserId(authorization);
    }
}
