package org.app.notificationservice.controller;

import org.app.notificationservice.dto.NotificationRequestDto;
import org.app.notificationservice.dto.NotificationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public NotificationResponseDto sendNotification(
            @RequestBody NotificationRequestDto request) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getEmail());
            message.setSubject("User Created Successfully");

            message.setText("Hello " + request.getFirstName() +
                    ",\n\nYour user account has been created successfully.\n\n" +
                    "Welcome to our application!");

            mailSender.send(message);

            return new NotificationResponseDto(
                    "SUCCESS",
                    "User created successfully and notification sent"
            );

        } catch (Exception e) {
            return new NotificationResponseDto(
                    "FAILED",
                    "User created but email sending failed: " + e.getMessage()
            );
        }
    }
}