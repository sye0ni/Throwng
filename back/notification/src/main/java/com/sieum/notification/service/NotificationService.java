package com.sieum.notification.service;

import static com.sieum.notification.domain.enums.Usage.CONTENT;
import static com.sieum.notification.domain.enums.Usage.COUPON;

import com.sieum.notification.controller.feign.TokenAuthClient;
import com.sieum.notification.domain.ContentNotification;
import com.sieum.notification.domain.ContentNotificationLog;
import com.sieum.notification.domain.CouponNotification;
import com.sieum.notification.domain.CouponNotificationLog;
import com.sieum.notification.domain.field.BodyData;
import com.sieum.notification.dto.FcmMessage;
import com.sieum.notification.dto.UserInfo;
import com.sieum.notification.repository.*;
import com.sieum.notification.util.RedisUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final TokenAuthClient tokenAuthClient;
    private final RedisUtil redisUtil;
    private final FcmService fcmService;
    private final CouponNotificationRepository couponNotificationRepository;
    private final CouponNotificationLogRepository couponNotificationLogRepository;
    private final ContentNotificationRepository contentNotificationRepository;
    private final ContentNotificationLogRepository contentNotificationLogRepository;

    @Scheduled(cron = "0 0 16 * * *", zone = "Asia/Seoul")
    public void createCouponExpirationNotification() {

        final String key = "noti_coupon_expiration_" + LocalDate.now();
        final List<Long> userIdList = (List<Long>) redisUtil.getObject(key);

        if (userIdList == null) return;

        final List<UserInfo> userInfoList =
                userIdList.stream()
                        .map(
                                userId ->
                                        UserInfo.builder()
                                                .userId(userId)
                                                .fcmToken(getUserFcm(userId))
                                                .build())
                        .filter(userInfo -> userInfo.getFcmToken() != null)
                        .collect(Collectors.toList());

        final List<String> fcmTokenList =
                userInfoList.stream()
                        .map(UserInfo::getFcmToken)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        final CouponNotification couponNotification =
                couponNotificationRepository.findByUsage(COUPON.getValue());

        final FcmMessage fcmMessage =
                FcmMessage.builder()
                        .title(couponNotification.getBodyData().getTitle())
                        .link(couponNotification.getBodyData().getLink())
                        .body(couponNotification.getBodyData().getBody())
                        .image(couponNotification.getBodyData().getImage())
                        .build();

        fcmService.sendMessageTo(fcmTokenList, fcmMessage);

        userInfoList.stream()
                .filter(userInfo -> userInfo.getFcmToken() != null)
                .forEach(
                        userInfo -> {
                            couponNotificationLogRepository.save(
                                    CouponNotificationLog.builder()
                                            .time(LocalDateTime.now().plusHours(9))
                                            .userId(userInfo.getUserId())
                                            .couponNotification(couponNotification)
                                            .build());
                        });
    }

    @Scheduled(cron = "0 0 19 * * *", zone = "Asia/Seoul")
    public void createContentEngagementNotification() {
        final List<UserInfo> userInfoList = getUserFcmList();

        final List<String> fcmTokenList =
                userInfoList.stream()
                        .map(UserInfo::getFcmToken)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        final ContentNotification contentNotification =
                contentNotificationRepository.findByUsage(CONTENT.getValue());

        final FcmMessage fcmMessage =
                FcmMessage.builder()
                        .title(contentNotification.getBodyData().getTitle())
                        .link(contentNotification.getBodyData().getLink())
                        .body(contentNotification.getBodyData().getBody())
                        .image(contentNotification.getBodyData().getImage())
                        .build();

        fcmService.sendMessageTo(fcmTokenList, fcmMessage);

        userInfoList.stream()
                .filter(userInfo -> userInfo.getFcmToken() != null)
                .forEach(
                        userInfo -> {
                            contentNotificationLogRepository.save(
                                    ContentNotificationLog.builder()
                                            .time(LocalDateTime.now().plusHours(9))
                                            .userId(userInfo.getUserId())
                                            .contentNotification(contentNotification)
                                            .build());
                        });
    }

    // will be removed later
    public void couponNotificationSave() {
        final BodyData bodyData =
                BodyData.builder()
                        .title("내일 만료되는 미사용 쿠폰이 있어요! 🎫")
                        .body("마이 페이지에서 쿠폰을 확인해보세요")
                        .link("https://sieum.co.kr/user/mycoupons")
                        .image("https://throwng.s3.ap-northeast-2.amazonaws.com/logo.png")
                        .build();

        couponNotificationRepository.save(
                CouponNotification.builder()
                        .usage(COUPON.getValue())
                        .bodyData(bodyData)
                        .category(COUPON.getKr())
                        .build());
    }

    // will be removed later
    public void contentNotificationSave() {
        final BodyData bodyData =
                BodyData.builder()
                        .title("오늘의 컨텐츠에 참여 하셨나요? 🎮")
                        .body("퀴즈와 게임으로 쿠폰 받고 쓰롱하러 가기")
                        .link("https://sieum.co.kr/content")
                        .image("https://throwng.s3.ap-northeast-2.amazonaws.com/logo.png")
                        .build();

        contentNotificationRepository.save(
                ContentNotification.builder()
                        .usage(CONTENT.getValue())
                        .bodyData(bodyData)
                        .category(CONTENT.getKr())
                        .build());
    }

    public String getUserFcm(final long userId) {
        return tokenAuthClient.getUserFcm(userId);
    }

    public List<UserInfo> getUserFcmList() {
        return tokenAuthClient.getUseFcmList();
    }
}
