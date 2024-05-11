package com.sieum.notification.service;

import static com.sieum.notification.domain.enums.Usage.CONTENT_QUIZ;

import com.sieum.notification.controller.feign.TokenAuthClient;
import com.sieum.notification.domain.ContentNotification;
import com.sieum.notification.domain.ContentNotificationLog;
import com.sieum.notification.domain.Notification;
import com.sieum.notification.domain.field.BodyData;
import com.sieum.notification.dto.FcmMessage;
import com.sieum.notification.dto.UserInfo;
import com.sieum.notification.dto.response.NotificationResponse;
import com.sieum.notification.repository.ContentNotificationLogRepository;
import com.sieum.notification.repository.ContentNotificationRepository;
import com.sieum.notification.repository.NotificationRepository;
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
    private final NotificationRepository notificationRepository;
    private final ContentNotificationRepository contentNotificationRepository;
    private final ContentNotificationLogRepository contentNotificationLogRepository;

    public List<NotificationResponse> mongoSearchTest(final String notificationName) {
        final List<Notification> notifications =
                notificationRepository.findAllByName(notificationName);
        return notifications.stream().map(NotificationResponse::of).collect(Collectors.toList());
    }

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

        final ContentNotification contentNotification =
                contentNotificationRepository.findByUsage(CONTENT_QUIZ.getValue());

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

    // will be removed soon
    public void couponNotificationSave() {
        final BodyData bodyData =
                BodyData.builder()
                        .title("내일 만료되는 미사용 쿠폰이 있어요!")
                        .body("마이 페이지에서 쿠폰을 확인해보세요!")
                        .link("https://sieum.co.kr/user/mypage")
                        .image("https://throwng.s3.ap-northeast-2.amazonaws.com/logo.png")
                        .build();

        contentNotificationRepository.save(
                ContentNotification.builder()
                        .usage(CONTENT_QUIZ.getValue())
                        .bodyData(bodyData)
                        .category(CONTENT_QUIZ.getKr())
                        .build());
    }

    public String getUserFcm(final long userId) {
        return tokenAuthClient.getUserFcm(userId);
    }
}
