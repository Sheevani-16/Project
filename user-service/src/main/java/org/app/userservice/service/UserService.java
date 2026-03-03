package org.app.userservice.service;
import org.app.notificationservice.dto.NotificationRequestDto;
import org.app.notificationservice.dto.NotificationResponseDto;
import org.app.userservice.dto.UserNotificationResponseDto;
import org.app.userservice.dto.UserRequestdto;
import org.app.userservice.dto.UserResponsedto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserResponsedto createUser(UserRequestdto userRequestdto);
    UserResponsedto getUserById(Long id);
    List<UserResponsedto> getAllUsers();
    void deleteUser(Long id);
     UserNotificationResponseDto createUserAndNotify(UserRequestdto request);
}
