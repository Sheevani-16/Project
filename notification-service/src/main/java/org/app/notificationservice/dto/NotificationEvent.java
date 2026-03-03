package org.app.notificationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationEvent {

    private String userID;
    private  String email;
    private String phoneNumber;
    private String message;
    private NotificationType type;
}
