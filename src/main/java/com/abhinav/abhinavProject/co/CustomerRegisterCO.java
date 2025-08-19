package com.abhinav.abhinavProject.co;


import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRegisterCO {

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 20, message = "First name should be between 3 and 50 characters")
    String firstName;

    @Size(max = 20, message = "Middle name should be max 20 characters")
    String middleName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 20, message = "Last name should be between 3 and 20 characters")
    String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\d{10}$",
            message = "Phone number must be exactly 10 digits"
    )
    String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "Password must contain at least 1 lowercase, 1 uppercase, 1 number, and 1 special character"
    )
    String password;

    @NotBlank(message = "Confirm Password is required")
    String confirmPassword;

}
