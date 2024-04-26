package com.sieum.user.service;

import static com.sieum.user.common.CustomExceptionStatus.NOT_FOUND_ACCOUNT;

import com.sieum.user.controller.feign.MusicFeignClient;
import com.sieum.user.domain.User;
import com.sieum.user.dto.response.UserInfoResponse;
import com.sieum.user.exception.AuthException;
import com.sieum.user.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MusicFeignClient musicFeignClient;

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
}
