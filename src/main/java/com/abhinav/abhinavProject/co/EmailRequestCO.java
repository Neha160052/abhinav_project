package com.abhinav.abhinavProject.co;


import com.abhinav.abhinavProject.constant.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequestCO {

    @NotBlank(message = "{email.required}")
    @Pattern(regexp = Regex.EMAIL, message = "{email.invalid}")
    String email;
}
