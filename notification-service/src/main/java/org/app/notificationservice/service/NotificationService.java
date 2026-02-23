package org.app.notificationservice.service;

import org.app.notificationservice.dto.NotificationRequestDto;
import org.app.notificationservice.dto.NotificationResponseDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ✅ Non-static method for sending notifications
    public NotificationResponseDto sendNotification(NotificationRequestDto request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getEmail());
            message.setSubject("Account Created Successfully");
            message.setText("Hello " + request.getFirstName() +
                    ",\n\nYour account has been created successfully.\n\nWelcome!");

            mailSender.send(message);

            return new NotificationResponseDto("SUCCESS",
                    "Notification sent successfully to " + request.getEmail());
        } catch (Exception e) {
            return new NotificationResponseDto("FAILED",
                    "Notification failed: " + e.getMessage());
        }
    }
}