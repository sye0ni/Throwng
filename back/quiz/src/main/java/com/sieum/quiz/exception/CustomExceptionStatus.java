package com.sieum.quiz.exception;

import com.sieum.quiz.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomExceptionStatus implements BaseErrorCode {
    INTERNAL_SERVER_ERROR("InternalServer_500_1", "Server error"),
    INVALID_REQUEST("BadRequest_400_1", "Invalid request"),
    REQUEST_ERROR("NotValidInput_400_2", "Invalid input"),
    DUPLICATE_COUPON_REQUEST(
            "CouponDuplicationError_400_3", "Get a coupon once a day through contents"),
    INVALID_COUPON_ROUTE("CouponRouteError_400_4", "Wrong coupon route");
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder().reason(reason).code(code).build();
    }
}
