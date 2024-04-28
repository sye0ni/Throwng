package com.sieum.notification.service;

import com.sieum.notification.domain.Notification;
import com.sieum.notification.dto.request.NotificationRequest;
import com.sieum.notification.dto.response.NotificationResponse;
import com.sieum.notification.repository.NotificationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MongoTemplate mongoTemplate;
    private final NotificationRepository notificationRepository;

    public void mongoInsertTest(final NotificationRequest notificationRequest) {
        notificationRepository.save(
                Notification.builder()
                        .body(notificationRequest.getBody())
                        .title(notificationRequest.getTitle())
                        .image(notificationRequest.getImage())
                        .name(notificationRequest.getName())
                        .build());
    }

    public List<NotificationResponse> mongoSearchTest(final String notificationName) {
        final List<Notification> notifications =
                notificationRepository.findAllByName(notificationName);
        return notifications.stream().map(NotificationResponse::of).collect(Collectors.toList());
    }
}
