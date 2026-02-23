package org.app.userservice.service;
import org.app.notificationservice.dto.NotificationRequestDto;
import org.app.userservice.Config.WebClientConfig;
import org.app.userservice.dto.UserNotificationResponseDto;
import org.app.userservice.dto.UserRequestdto;
import org.app.userservice.dto.UserResponsedto;
import org.app.userservice.Exception.BadRequestException;
import org.app.userservice.Exception.ResourceNotFoundException;
import org.app.userservice.model.User;
import org.app.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private WebClientConfig webClientConfig;
    private RestTemplate restTemplate;

    public UserServiceImpl(UserRepository userRepository, WebClientConfig webClientConfig,RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.webClientConfig = webClientConfig;
        this.restTemplate = restTemplate;
    }

    @Override
    public UserResponsedto createUser(UserRequestdto userRequestdto) {

        if (userRequestdto.getFirstName() == null || userRequestdto.getFirstName().isEmpty()) {
            throw new BadRequestException("First Name is required");
        }

        if (userRequestdto.getEmail() == null || userRequestdto.getEmail().isEmpty()) {
            throw new BadRequestException("Email is required");
        }

        if (userRequestdto.getMobileNumber() == null) {
            throw new BadRequestException("Mobile Number is required");
        }

        Optional<User> existingEmail = userRepository.findByEmail(userRequestdto.getEmail());
        if (existingEmail.isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setFirstName(userRequestdto.getFirstName());
        user.setLastName(userRequestdto.getLastName());
        user.setMobileNumber(userRequestdto.getMobileNumber());
        user.setEmail(userRequestdto.getEmail());
        user.setCity(userRequestdto.getCity());
        user.setState(userRequestdto.getState());
        user.setZipCode(userRequestdto.getZipCode());

        User savedUser = userRepository.save(user);

        UserResponsedto response = new UserResponsedto();
        response.setId(savedUser.getId());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setMobileNumber(savedUser.getMobileNumber());
        response.setEmail(savedUser.getEmail());
        response.setCity(savedUser.getCity());
        response.setState(savedUser.getState());
        response.setZipCode(savedUser.getZipCode());

        return response;
    }

    @Override
    public UserResponsedto getUserById(Long id) {

        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid User ID");
        }

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }

        User user = optionalUser.get();

        UserResponsedto response = new UserResponsedto();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setMobileNumber(user.getMobileNumber());
        response.setEmail(user.getEmail());
        response.setCity(user.getCity());
        response.setState(user.getState());
        response.setZipCode(user.getZipCode());

        return response;
    }

    @Override
    public List<UserResponsedto> getAllUsers() {

        List<User> users = userRepository.findAll();
        List<UserResponsedto> responseList = new ArrayList<>();

        for (User user : users) {

            UserResponsedto response = new UserResponsedto();
            response.setId(user.getId());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setMobileNumber(user.getMobileNumber());
            response.setEmail(user.getEmail());
            response.setCity(user.getCity());
            response.setState(user.getState());
            response.setZipCode(user.getZipCode());

            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public void deleteUser(Long id) {

        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid User ID");
        }

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
    }

    @Override
    public UserNotificationResponseDto createUserAndNotify(UserRequestdto request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setZipCode(request.getZipCode());

        user = userRepository.save(user);

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
                user.getFirstName(),
                user.getEmail()
        );

        String notificationStatus;

        try {
            restTemplate.postForEntity(
                    "http://notification-service/notifications",
                    notificationRequestDto,
                    String.class
            );
            notificationStatus = "Notification sent successfully";
        } catch (Exception e) {
            notificationStatus = "Notification failed: " + e.getMessage();
        }

        UserResponsedto userResponse = new UserResponsedto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMobileNumber(),
                user.getEmail(),
                user.getCity(),
                user.getState(),
                user.getZipCode()
        );
        return new UserNotificationResponseDto(userResponse, notificationStatus);
    }
//    @Override
//    public UserResponsedto createUserAndNotify(UserRequestdto userRequestdto) {
//
//        User user = new User();
//        user.setFirstName(userRequestdto.getFirstName());
//        user.setLastName(userRequestdto.getLastName());
//        user.setEmail(userRequestdto.getEmail());
//        user.setMobileNumber(userRequestdto.getMobileNumber());
//        user.setCity(userRequestdto.getCity());
//        user.setState(userRequestdto.getState());
//        user.setZipCode(userRequestdto.getZipCode());
//
//        user = userRepository.save(user);
//
//        WebClient webClient = WebClient.create("http://notification-service");
//
//        NotificationRequestDto payload =
//                new NotificationRequestDto(user.getFirstName(), user.getEmail());
//
//        try {
//            webClient.post()
//                    .uri("/notifications")
//                    .bodyValue(payload)
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return new UserResponsedto(
//                user.getId(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getMobileNumber(),
//                user.getEmail(),
//                user.getCity(),
//                user.getState(),
//                user.getZipCode()
//        );
//    }
}