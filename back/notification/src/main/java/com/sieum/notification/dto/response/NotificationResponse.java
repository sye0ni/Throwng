package com.sieum.notification.dto.response;

import com.sieum.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private String title;
    private String body;
    private String image;

    public static NotificationResponse of(final Notification notification) {
        return NotificationResponse.builder()
                .title(notification.getTitle())
                .body(notification.getBody())
                .image(notification.getImage())
                .build();
    }
}
