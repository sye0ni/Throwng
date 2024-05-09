package com.sieum.user.service;

import static com.sieum.user.common.CustomExceptionStatus.*;

import com.sieum.user.controller.feign.MusicFeignClient;
import com.sieum.user.controller.feign.QuizFeignClient;
import com.sieum.user.domain.LevelHistory;
import com.sieum.user.domain.User;
import com.sieum.user.domain.enums.Level;
import com.sieum.user.dto.request.CouponNickNameRequest;
import com.sieum.user.dto.request.CouponStatusRequest;
import com.sieum.user.dto.request.CouponValidationRequest;
import com.sieum.user.dto.request.FcmTokenRequest;
import com.sieum.user.dto.response.*;
import com.sieum.user.exception.AuthException;
import com.sieum.user.exception.BadRequestException;
import com.sieum.user.exception.FeignClientException;
import com.sieum.user.repository.LevelHistoryRepository;
import com.sieum.user.repository.UserRepository;
import feign.FeignException;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MusicFeignClient musicFeignClient;
    private final LoginService loginService;
    private final QuizFeignClient quizFeignClient;
    private final LevelHistoryRepository levelHistoryRepository;

    public UserInfoResponse getUserLevel(long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new AuthException(NOT_FOUND_ACCOUNT));

        return UserInfoResponse.of(
                user,
                musicFeignClient.countThrownSong(userId),
                musicFeignClient.countPickUpSong(userId));
    }

    public UserLevelInfoResponse getLimitAccount(long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new AuthException(NOT_FOUND_ACCOUNT));

        LevelHistory levelHistory =
                levelHistoryRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());

        if (levelHistory == null) {
            throw new BadRequestException(NOT_FOUND_LEVEL_HISTORY_ID);
        }

        if (!user.getViolation().equals("NONE")) {
            throw new AuthException(VIOLATE_ACCOUNT);
        }

        return UserLevelInfoResponse.of(userId, levelHistory.getLevel().getThrowngLimit());
    }

    public int getUserLevelInfo(long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new AuthException(NOT_FOUND_ACCOUNT));

        return Level.getCount(user.getLevel());
    }

    public List<ThrownSongResponse> getThrownSong(final long userId) {
        return musicFeignClient.getThrwonSong(userId);
    }

    public List<PickedUpSongResponse> getPickedUpSong(final long userId) {
        return musicFeignClient.getPickedUpSong(userId);
    }

    public void createUserFcmToken(
            final String accessToken, final FcmTokenRequest fcmTokenRequest) {
        final long userId = loginService.getUsername(accessToken);
        final User user = userRepository.findById(userId).get();
        user.setFcmToken(fcmTokenRequest.getFcmToken());
        userRepository.save(user);
    }

    public List<String> getUserFcmList() {
        return userRepository.findByFcmTokenIsNotNull().stream()
                .map(User::getFcmToken)
                .collect(Collectors.toList());
    }

    public List<CouponeInquiryResponse> getUserCouponHistory(final long userId) {
        return quizFeignClient.getCouponHistory(userId);
    }

    public void modifyNickName(
            final long userId, final CouponNickNameRequest couponNickNameRequest) {
        // ValidVating Coupon
        CouponValidationRequest couponValidationRequest =
                CouponValidationRequest.of(
                        userId,
                        couponNickNameRequest.getCouponId(),
                        couponNickNameRequest.getType());

        try {
            if (quizFeignClient.validateCoupon(couponValidationRequest)) {
                final User user =
                        userRepository
                                .findById(userId)
                                .orElseThrow(
                                        () -> new BadRequestException(NOT_AUTHENTICATED_ACCOUNT));

                user.updateNickName(couponNickNameRequest.getNickName());
                quizFeignClient.modifyCouponStatus(CouponStatusRequest.of(couponValidationRequest));
            }
        } catch (FeignException feignException) {
            throw new FeignClientException(NOT_USE_COUPON_FROM_FEIGN);
        }
    }

    public String getUserFcmToken(final long userId) {
        return userRepository.findById(userId).get().getFcmToken();
    }
}
