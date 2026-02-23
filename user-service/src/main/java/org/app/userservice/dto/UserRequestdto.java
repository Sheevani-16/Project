package org.app.userservice.dto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestdto {
    private String firstName;
    private String lastName;
    private Long mobileNumber;
    private String email;
    private String city;
    private String state;
    private Long zipCode;
}
