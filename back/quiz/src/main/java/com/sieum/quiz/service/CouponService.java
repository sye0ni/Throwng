package com.sieum.quiz.service;

import static com.sieum.quiz.exception.CustomExceptionStatus.DUPLICATE_COUPON_REQUEST;

import com.sieum.quiz.controller.feign.NotificationAuthClient;
import com.sieum.quiz.controller.feign.UserAuthClient;
import com.sieum.quiz.domain.Coupon;
import com.sieum.quiz.domain.CouponHistory;
import com.sieum.quiz.domain.enums.CouponRoute;
import com.sieum.quiz.domain.enums.CouponType;
import com.sieum.quiz.dto.request.CouponNotificationRequest;
import com.sieum.quiz.dto.response.CouponHistoryNewestResponse;
import com.sieum.quiz.dto.response.CouponeInquiryResponse;
import com.sieum.quiz.dto.response.CreateCouponResponse;
import com.sieum.quiz.exception.BadRequestException;
import com.sieum.quiz.repository.CouponHistoryQueryDSLRepository;
import com.sieum.quiz.repository.CouponHistoryRepository;
import com.sieum.quiz.repository.CouponReposistory;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.sieum.quiz.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private static final Random random = new Random();
    private final UserAuthClient userAuthClient;
    private final NotificationAuthClient notificationAuthClient;
    private final RedisUtil redisUtil;
    private final CouponReposistory couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;
    private final CouponHistoryQueryDSLRepository couponHistoryQueryDSLRepository;

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

        if (!couponType.equals("BOOM")) {
            createCouponHistory(coupon);
        }

        return CreateCouponResponse.of(coupon);
    }

    public long getCurrentUserId(final String authorization) {
        return userAuthClient.getUserId(authorization);
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
//    @Scheduled(cron="0 0 4 * * *", zone="Asia/Seoul")
    public void sendCouponExpirationNotification() {

        final List<CouponHistoryNewestResponse> couponHistoryNewestResponses=couponHistoryQueryDSLRepository.findNewestCouponHistory();

        final List<Long> couponIdList =couponHistoryNewestResponses.stream()
                .filter(couponHistoryNewestResponse -> couponHistoryQueryDSLRepository.findExpirationCouponHistory(
                        CouponHistoryNewestResponse.builder()
                                .couponId(couponHistoryNewestResponse.getCouponId())
                                .createdAt(couponHistoryNewestResponse.getCreatedAt()).build()
                ) != 0).map(
                        CouponHistoryNewestResponse::getCouponId
                ).collect(Collectors.toList());


        final List<Long> userIdList=couponIdList.stream()
                .map(
                      couponId->  couponRepository.findById(couponId).get().getUserId()
                ).distinct().collect(Collectors.toList());

        final String key="noti_coupon_expiration_"+LocalDate.now();
        redisUtil.setObject(key,userIdList);

//        sendCouponExpirationUserIdList(userIdList);

    }

    public void sendCouponExpirationUserIdList(final List<Long> userIdList) {
        notificationAuthClient.sendCouponExpirationUserIdList(userIdList);
    }


}
