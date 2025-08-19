package com.abhinav.abhinavProject.co;

import com.abhinav.abhinavProject.constant.Regex;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerRegisterCO {


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
            regexp = Regex.CONTACT,
            message = "Phone number must be exactly 10 digits"
    )
    String companyContact;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters")
    @Pattern(
            regexp = Regex.PASSWORD,
            message = "Password must contain at least 1 lowercase, 1 uppercase, 1 number, and 1 special character"
    )
    String password;

    @NotBlank(message = "Confirm Password is required")
    String confirmPassword;

    @NotBlank
    @Pattern(regexp = Regex.GST)
    String gst;

    @NotBlank(message = "Company name is required")
    @Size(min = 3, max = 20, message = "Name should be between 3 and 50 characters")
    String companyName;

    @Valid
    AddressDTO companyAddress;
}
