package com.sieum.quiz.validator;

import static com.sieum.quiz.exception.CustomExceptionStatus.*;

import com.sieum.quiz.domain.Coupon;
import com.sieum.quiz.domain.CouponHistory;
import com.sieum.quiz.dto.request.CouponValidationRequest;
import com.sieum.quiz.exception.BadRequestException;
import com.sieum.quiz.repository.CouponHistoryRepository;
import com.sieum.quiz.repository.CouponReposistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponValidator {

    private final CouponReposistory couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;
    private final String COUPON_STATUS = "NONE";

    public Boolean validateCoupon(final CouponValidationRequest couponValidationRequest) {
        Coupon coupon =
                findCurrentCoupon(
                        couponValidationRequest.getCouponId(), couponValidationRequest.getUserId());
        if (!coupon.getCouponType().equals(couponValidationRequest.getType())) {
            throw new BadRequestException(NOT_MATCH_COUPON_TYPE);
        }

        findCurrentCouponHistoryByCurrentCoupon(couponValidationRequest.getCouponId());

        return true;
    }

    public Coupon findCurrentCoupon(final long couponId, final long userId) {
        return couponRepository
                .findByIdAndUserId(couponId, userId)
                .orElseThrow(() -> new BadRequestException(NOT_MATCH_COUPON_USER));
    }

    public void findCurrentCouponHistoryByCurrentCoupon(final long couponId) {
        CouponHistory couponHistory =
                couponHistoryRepository.findTopByCouponIdOrderByCreatedAtDesc(couponId);

        if (couponHistory == null) {
            throw new BadRequestException(NOT_FOUND_COUPON_ID);
        } else {
            if (!couponHistory.getCouponStatus().equals(COUPON_STATUS)) {
                throw new BadRequestException(NOT_USE_COUPON_STATUS);
            }
        }
    }
}
