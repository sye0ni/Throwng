package com.sieum.user.service;

import com.sieum.user.infrastructure.JwtProvider;
import com.sieum.user.util.CreateOTPUtil;
import com.sieum.user.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WatchService {

    private final CreateOTPUtil createOTPUtil;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private static final int OTP_VALIDITY = 120;

    /*
    Need to think about how to handle retry logic if it fails more than once
     */
    public String createOTP(final String accessToken) {
        final String userId = jwtProvider.getUserId(accessToken);
        final String otp = createOTPUtil.createOTP().toString();
        final String key = redisUtil.getData(otp);
        redisUtil.setDataExpire(otp, userId, OTP_VALIDITY);
        return otp;
    }
}
