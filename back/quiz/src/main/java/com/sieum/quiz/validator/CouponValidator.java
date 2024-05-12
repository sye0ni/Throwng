package com.sieum.quiz.validator;

import static com.sieum.quiz.exception.CustomExceptionStatus.*;

import com.sieum.quiz.controller.feign.UserAuthClient;
import com.sieum.quiz.domain.Coupon;
import com.sieum.quiz.domain.CouponHistory;
import com.sieum.quiz.dto.request.CouponValidationRequest;
import com.sieum.quiz.exception.BadRequestException;
import com.sieum.quiz.repository.CouponHistoryRepository;
import com.sieum.quiz.repository.CouponReposistory;
import com.sieum.quiz.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponValidator {

    private final CouponReposistory couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;
    private final UserAuthClient userAuthClient;
    private final RedisUtil redisUtil;
    private final String COUPON_USE_STATUS = "NONE";
    private final String EXCEPT_COUPON_TYPE = "NICKNAME";
    private final String IN_USE_COUPON = "INUSE";
    private final String WIDE = "WIDE";
    private final String THROWNG_INF = "THROWNG_INF";
    private final String THROWNG_TWICE = "THROWNG_TWICE";
    private final String THROWNG_LEVEL = "THROWNG_LEVEL";
    private final String THROWNG_FIVE = "THROWNG_FIVE";
    private final int THROWNG_FIDVE_LIMIT_COUNT = 5;
    private final int TWICE = 2;

    public Boolean validateCoupon(final CouponValidationRequest couponValidationRequest) {

        Coupon coupon =
                couponRepository
                        .findById(couponValidationRequest.getCouponId())
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_COUPON_ID));

        // Match coupon user with the person issued
        if (coupon.getUserId() != couponValidationRequest.getUserId()) {
            throw new BadRequestException(NOT_MATCH_COUPON_USER);
        }

        // Verify that the type of coupon requested and the type of coupon verified by couponId are
        // the same
        if (!couponValidationRequest.getCouponType().equals(coupon.getCouponType())) {
            throw new BadRequestException(NOT_MATCH_COUPON_TYPE);
        }

        // Verify that the coupon is available
        CouponHistory couponHistory = checkCouponStatus(couponValidationRequest.getCouponId());

        if (!couponHistory.getCouponStatus().equals(COUPON_USE_STATUS)) {
            throw new BadRequestException(NOT_USE_COUPON_STATUS);
        }

        // Change status according to the type of coupon
        if (changeCouponStatus(coupon)) {
            issueRedisKey(coupon, couponValidationRequest.getUserId());
        }

        return true;
    }

    public CouponHistory checkCouponStatus(final long couponId) {
        CouponHistory couponHistory =
                couponHistoryRepository.findTopByCouponIdOrderByCreatedAtDesc(couponId);

        if (couponHistory == null) {
            throw new BadRequestException(NOT_FOUND_COUPON_ID);
        }

        return couponHistory;
    }

    public boolean changeCouponStatus(final Coupon coupon) {

        if (!coupon.getCouponType().equals(EXCEPT_COUPON_TYPE)) {
            couponHistoryRepository.save(
                    CouponHistory.builder().couponStatus(IN_USE_COUPON).coupon(coupon).build());
            return true;
        }
        return false;
    }

    public void issueRedisKey(final Coupon coupon, final long userId) {

        if (coupon.getCouponType().equals(WIDE)) {
            final String key = userId + "_" + coupon.getCouponType();
            redisUtil.setData(key, coupon.getCouponType());
            final String couponKey = userId + "_COUPON_ID_WIDE";
            redisUtil.setObject(couponKey, coupon.getId());

        } else {
            final String key = userId + "_" + "THROWNG";
            redisUtil.setObject(key, coupon.getCouponType());

            final String couponKey = userId + "_COUPON_ID_THROWNG";
            redisUtil.setObject(couponKey, coupon.getId());

            final String deatilKey = userId + "_" + coupon.getCouponType();

            if (coupon.getCouponType().equals(THROWNG_FIVE)) {
                redisUtil.setObject(deatilKey, THROWNG_FIDVE_LIMIT_COUNT);
            } else if (coupon.getCouponType().equals(THROWNG_LEVEL)) {
                redisUtil.setObject(deatilKey, userAuthClient.getLevelThrowngCount(userId));
            } else if (coupon.getCouponType().equals(THROWNG_TWICE)) {
                redisUtil.setObject(deatilKey, userAuthClient.getLevelThrowngCount(userId) * TWICE);
            }
        }
    }
}