package com.sieum.quiz.dto.response;

import com.sieum.quiz.domain.Coupon;
import com.sieum.quiz.domain.CouponHistory;
import com.sieum.quiz.domain.enums.CouponStatus;
import com.sieum.quiz.domain.enums.CouponType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponeInquiryResponse {
    private static final int EXPIRATION_PERIOD = 7;

    @Schema(description = "coupon type")
    private Long id;

    @Schema(description = "coupon name")
    private String name;

    @Schema(description = "coupon description")
    private String description;

    @Schema(description = "coupon endDate")
    private LocalDateTime endDate;

    @Schema(description = "coupon status")
    private String status;

    public static LocalDateTime createEndDate(LocalDateTime createdAt) {
        return createdAt.plusDays(EXPIRATION_PERIOD);
    }

    public static CouponeInquiryResponse of(Coupon coupon, CouponHistory couponHistory) {
        return new CouponeInquiryResponse(
                coupon.getId(),
                CouponType.valueOf(coupon.getCouponType()).getName(),
                CouponType.valueOf(coupon.getCouponType()).getDescription(),
                createEndDate(coupon.getCreatedAt()),
                CouponStatus.valueOf(couponHistory.getCouponStatus()).getKr());
    }
}
