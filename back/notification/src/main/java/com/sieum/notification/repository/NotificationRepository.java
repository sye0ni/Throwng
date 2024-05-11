package com.sieum.notification.repository;

import com.sieum.notification.domain.Notification;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByName(final String notificationName);
}
