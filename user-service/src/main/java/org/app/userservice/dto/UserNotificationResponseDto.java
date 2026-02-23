package org.app.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserNotificationResponseDto {
    private UserResponsedto user;
    private String notificationStatus;
}