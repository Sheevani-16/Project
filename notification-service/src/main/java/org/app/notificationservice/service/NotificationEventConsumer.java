package org.app.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.app.notificationservice.dto.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {

        private final NotificationService notificationService;

        @KafkaListener(topics = "notification-events", groupId = "notification-group")
        public void consume(NotificationEvent event) {
            notificationService.sendNotification(event);
        }
    }
