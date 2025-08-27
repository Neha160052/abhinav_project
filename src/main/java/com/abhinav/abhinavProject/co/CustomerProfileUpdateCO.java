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
    String firstName;

    @Size(max = 20, message = "Middle name should be max 20 characters")
    String middleName;

    @Size(min = 3, max = 20, message = "Last name should be between 3 and 20 characters")
    String lastName;

    @Pattern(
            regexp = Regex.CONTACT,
            message = "Phone number must be exactly 10 digits"
    )
    String contact;
}
