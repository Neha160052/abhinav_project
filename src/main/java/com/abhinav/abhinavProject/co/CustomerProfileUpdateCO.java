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

    @Size(min = 3, max = 20, message = "{user.firstname.size}")
    @Pattern(
            regexp = Regex.NAME,
            message = "{user.firstname.pattern}"
    )
    String firstName;

    @Size(min = 3, max = 20, message = "{user.firstname.size}")
    @Pattern(
            regexp = Regex.NAME,
            message = "{user.firstname.pattern}"
    )
    String middleName;

    @Size(min = 3, max = 20, message = "{user.lastname.size}")
    @Pattern(
            regexp = Regex.NAME,
            message = "{user.lastname.pattern}"
    )
    String lastName;

    @Pattern(
            regexp = Regex.CONTACT,
            message = "{user.phone.pattern}"
    )
    String contact;
}
