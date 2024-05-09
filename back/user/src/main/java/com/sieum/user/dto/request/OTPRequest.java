package com.sieum.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class OTPRequest {

    @NotNull
    @Schema(description = "otp code")
    private String otp;
}
