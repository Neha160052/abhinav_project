package com.abhinav.abhinavProject.co;

import com.abhinav.abhinavProject.constant.Regex;
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
public class ResetPasswordCO {

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters")
    @Pattern(
            regexp = Regex.PASSWORD,
            message = "Password must contain at least 1 lowercase, 1 uppercase, 1 number, and 1 special character"
    )
    String password;

    @NotBlank(message = "Confirm Password is required")
    @Size(min = 8, max = 15, message = "Confirm Password must be between 8 and 15 characters")
    String confirmPassword;

}
