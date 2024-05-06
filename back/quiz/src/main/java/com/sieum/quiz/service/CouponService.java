package com.sieum.quiz.service;

import static com.sieum.quiz.exception.CustomExceptionStatus.DUPLICATE_COUPON_REQUEST;

import com.sieum.quiz.controller.feign.TokenAuthClient;
import com.sieum.quiz.domain.Coupon;
import com.sieum.quiz.domain.CouponHistory;
import com.sieum.quiz.domain.enums.CouponRoute;
import com.sieum.quiz.domain.enums.CouponType;
import com.sieum.quiz.dto.response.CreateCouponResponse;
import com.sieum.quiz.exception.BadRequestException;
import com.sieum.quiz.repository.CouponHistoryRepository;
import com.sieum.quiz.repository.CouponReposistory;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
    private static final Random random = new Random();
    private final TokenAuthClient tokenAuthClient;
    private final CouponReposistory couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;

    public CreateCouponResponse createCoupon(final long userId, final String route) {
        final String couponRoute = CouponRoute.findByName(route);

        if (couponRepository.existsByCreatedAtAfterAndRouteAndUserId(
                LocalDate.now().atStartOfDay(), couponRoute, userId)) {
            throw new BadRequestException(DUPLICATE_COUPON_REQUEST);
        }

        final String couponType = String.valueOf(generateCoupon().get());

        final Coupon coupon =
                couponRepository.save(
                        Coupon.builder()
                                .userId(userId)
                                .couponType(couponType)
                                .route(couponRoute)
                                .build());
        createCouponHistory(coupon);
        return CreateCouponResponse.of(coupon);
    }

    public long getCurrentUserId(final String authorization) {
        return tokenAuthClient.getUserId(authorization);
    }

    private Optional<CouponType> generateCoupon() {
        final double probability = random.nextDouble();
        return EnumSet.allOf(CouponType.class).stream()
                .filter(couponType -> couponType.getProbability() >= probability)
                .findFirst();
    }

    public void createCouponHistory(final Coupon coupon) {
        couponHistoryRepository.save(
                CouponHistory.builder().coupon(coupon).couponStatus("NONE").build());
    }
}
