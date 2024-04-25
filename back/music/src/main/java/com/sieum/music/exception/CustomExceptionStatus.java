package com.sieum.music.exception;

import com.sieum.music.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomExceptionStatus implements BaseErrorCode {
    INTERNAL_SERVER_ERROR("InternalServer_500_1", "Server error"),
    INVALID_REQUEST("BadRequest_400_1", "Invalid request"),
    REQUEST_ERROR("NotValidInput_400_2", "Invalid input"),
    NOT_FOUND_THROW_ITEM_ID("THROW_400_1", "No throw item with the requested id"),
    NOT_FOUND_KEY_WORD("Search_400_1", "No search key word");

    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder().reason(reason).code(code).build();
    }
}
