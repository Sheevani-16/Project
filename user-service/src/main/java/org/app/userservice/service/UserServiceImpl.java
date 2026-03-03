package org.app.userservice.service;
import org.app.notificationservice.dto.NotificationEvent;
import org.app.notificationservice.dto.NotificationRequestDto;
import org.app.notificationservice.dto.NotificationResponseDto;
import org.app.notificationservice.dto.NotificationType;
import org.app.userservice.dto.UserNotificationResponseDto;
import org.app.userservice.dto.UserRequestdto;
import org.app.userservice.dto.UserResponsedto;
import org.app.userservice.Exception.BadRequestException;
import org.app.userservice.Exception.ResourceNotFoundException;
import org.app.userservice.model.User;
import org.app.userservice.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RestTemplate restTemplate;
    private NotificationEventPublisher notificationEventPublisher;

    public UserServiceImpl(UserRepository userRepository, NotificationEventPublisher notificationEventPublisher) {
        this.userRepository = userRepository;
        this.notificationEventPublisher = notificationEventPublisher;
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

        // Publish Kafka event
        NotificationEvent event = new NotificationEvent(
                user.getId().toString(),
                user.getEmail(),
                user.getMobileNumber() != null ? user.getMobileNumber().toString() : null,
                "Hello " + user.getFirstName() + ", your account has been created successfully.",
                NotificationType.EMAIL
        );

        notificationEventPublisher.sendNotification(event);

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

        // Since notification is async now, status is "QUEUED"
        return new UserNotificationResponseDto(userResponse, "QUEUED: Notification event published to Kafka");
    }
}