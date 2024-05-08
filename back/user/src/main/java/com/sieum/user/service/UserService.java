package com.sieum.user.service;

import static com.sieum.user.common.CustomExceptionStatus.NOT_FOUND_ACCOUNT;
import static com.sieum.user.common.CustomExceptionStatus.VIOLATE_ACCOUNT;

import com.sieum.user.controller.feign.MusicFeignClient;
import com.sieum.user.controller.feign.QuizFeignClient;
import com.sieum.user.domain.User;
import com.sieum.user.domain.enums.Level;
import com.sieum.user.dto.request.FcmTokenRequest;
import com.sieum.user.dto.response.*;
import com.sieum.user.exception.AuthException;
import com.sieum.user.repository.UserRepository;
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

        if (!user.getViolation().equals("NONE")) {
            throw new AuthException(VIOLATE_ACCOUNT);
        }

        return UserLevelInfoResponse.of(userId, Level.getCount(user.getLevel()));
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

    public String getUserFcmToken(final long userId) {
        return userRepository.findById(userId).get().getFcmToken();
    }
}
