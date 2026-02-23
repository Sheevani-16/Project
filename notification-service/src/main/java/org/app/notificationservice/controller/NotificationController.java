package org.app.notificationservice.controller;

import org.app.notificationservice.dto.NotificationRequestDto;
import org.app.notificationservice.dto.NotificationResponseDto;
import org.app.notificationservice.service.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public NotificationResponseDto sendNotification(@RequestBody NotificationRequestDto request) {
        return notificationService.sendNotification(request);
    }
}