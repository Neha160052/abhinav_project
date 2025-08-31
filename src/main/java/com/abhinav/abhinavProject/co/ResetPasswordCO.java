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

    @NotBlank(message = "{password.required}")
    @Size(min = 8, max = 15, message = "{password.size}")
    @Pattern(
            regexp = Regex.PASSWORD,
            message = "{password.pattern}"
    )
    String password;

    @NotBlank(message = "{user.confirmpassword.required}")
    @Size(min = 8, max = 15, message = "{user.confirmpassword.size}")
    String confirmPassword;

}
