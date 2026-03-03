package org.app.notificationservice.service;

import org.app.notificationservice.dto.NotificationEvent;

public interface NotificationService {
    void sendNotification(NotificationEvent event);
}
