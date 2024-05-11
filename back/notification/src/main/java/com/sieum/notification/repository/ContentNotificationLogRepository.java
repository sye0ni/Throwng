package com.sieum.notification.repository;

import com.sieum.notification.domain.ContentNotificationLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentNotificationLogRepository
        extends MongoRepository<ContentNotificationLog, Long> {}
