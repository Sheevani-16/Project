package org.app.userservice.controller;
import org.app.userservice.dto.UserNotificationResponseDto;
import org.app.userservice.dto.UserRequestdto;
import org.app.userservice.dto.UserResponsedto;
import org.app.userservice.service.UserService;
import org.app.userservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public UserResponsedto createUser(@RequestBody UserRequestdto request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserResponsedto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserResponsedto> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
    @PostMapping("/notify")
    public UserNotificationResponseDto createUserAndNotify(
            @RequestBody UserRequestdto request) {

        return userService.createUserAndNotify(request);
    }
}