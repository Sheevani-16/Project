package org.app.userservice.service;


import lombok.RequiredArgsConstructor;
import org.app.notificationservice.dto.NotificationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;

    public void sendNotification(NotificationEvent event) {
        // key = userId ensures same-user ordering
        kafkaTemplate.send(topic, event.getUserID(), event);
    }
}
