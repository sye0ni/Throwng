package com.sieum.quiz.service;

import static com.sieum.quiz.exception.CustomExceptionStatus.DUPLICATE_COUPON_REQUEST;

import com.sieum.quiz.controller.feign.TokenAuthClient;
import com.sieum.quiz.domain.Coupon;
import com.sieum.quiz.domain.CouponHistory;
import com.sieum.quiz.domain.enums.CouponRoute;
import com.sieum.quiz.domain.enums.CouponType;
import com.sieum.quiz.dto.request.CouponNotificationRequest;
import com.sieum.quiz.dto.response.CouponeInquiryResponse;
import com.sieum.quiz.dto.response.CreateCouponResponse;
import com.sieum.quiz.exception.BadRequestException;
import com.sieum.quiz.repository.CouponHistoryRepository;
import com.sieum.quiz.repository.CouponReposistory;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<CouponeInquiryResponse> getCouponHistory(final long userId) {
        List<Coupon> coupons = couponRepository.findByUserId(userId);

        return coupons.stream()
                .filter(coupon -> !coupon.getCouponType().equals("BOOM"))
                .map(
                        coupon ->
                                CouponeInquiryResponse.of(
                                        coupon,
                                        couponHistoryRepository
                                                .findTopByCouponIdOrderByCreatedAtDesc(
                                                        coupon.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
//    @Scheduled(cron="0 0 15 * * *", zone="Asia/Seoul")
    public void sendCouponExpirationNotification() {
        System.out.println("스케줄러 가동!!");

        final List<CouponHistory> couponHistoryList=
                couponRepository.findAll().stream()
                        .map(
                                coupon->couponHistoryRepository.findTopByCouponIdOrderByCreatedAtDesc(coupon.getId())
                        ).collect(Collectors.toList());

        final List<Long> userIdList=
                couponHistoryList.stream()
                        .filter(
                                couponHistory -> couponHistory.getCouponStatus().equals("NONE") && couponHistory.getCreatedAt().toLocalDate().isEqual(LocalDate.now().minusDays(6))
                        )
                        .map(
                            couponHistory -> couponHistory.getCoupon().getUserId()
                        )
                        .distinct()
                        .collect(Collectors.toList());

        final List<CouponNotificationRequest> couponNotificationRequestList = userIdList.stream()
                .map(
                        userId->{
                            final String fcmToken=getUserFcm(userId);
                            return CouponNotificationRequest.builder()
                                    .userId(userId)
                                    .fcmToken(fcmToken).build();
                        }
                ).collect(Collectors.toList());

        System.out.println(couponNotificationRequestList);
    }

    public String getUserFcm(final long userId) {
        return tokenAuthClient.getUserFcm(userId);
    }

}
