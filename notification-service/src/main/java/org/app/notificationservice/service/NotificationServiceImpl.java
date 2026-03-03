package org.app.notificationservice.service;

import org.app.notificationservice.dto.NotificationEvent;
import org.app.notificationservice.dto.NotificationType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendNotification(NotificationEvent event) {
        if (event.getType() != NotificationType.EMAIL) return;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("Account Created Successfully");
        message.setText(event.getMessage());

        mailSender.send(message);
    }
}