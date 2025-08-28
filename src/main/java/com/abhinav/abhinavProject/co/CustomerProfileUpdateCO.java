package com.abhinav.abhinavProject.co;

import com.abhinav.abhinavProject.constant.Regex;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerProfileUpdateCO {

    @Size(min = 3, max = 20, message = "First name should be between 3 and 50 characters")
    @Pattern(
            regexp = Regex.NAME,
            message = "First name cannot be blank and only contain alphabets"
    )
    String firstName;

    @Size(min = 3, max = 20, message = "Middle name should be between 3 and 20 characters")
    @Pattern(
            regexp = Regex.NAME,
            message = "Middle name cannot be blank and only contain alphabets"
    )
    String middleName;

    @Size(min = 3, max = 20, message = "Last name should be between 3 and 20 characters")
    @Pattern(
            regexp = Regex.NAME,
            message = "Last name cannot be blank and only contain alphabets"
    )
    String lastName;

    @Pattern(
            regexp = Regex.CONTACT,
            message = "Phone number must be exactly 10 digits"
    )
    String contact;
}
