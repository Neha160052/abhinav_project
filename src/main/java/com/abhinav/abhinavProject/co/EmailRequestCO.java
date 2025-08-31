package com.abhinav.abhinavProject.co;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequestCO {

    @NotBlank(message = "{email.required}")
    @Email(message = "{email.invalid}")
    String email;
}
