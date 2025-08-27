package com.abhinav.abhinavProject.co;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressPatchDTO {

        @Length(message = "City name should be between 4 and 15 characters", min = 4, max = 15)
        String city;

        @Size(message = "State name length should be between 4 and 50 characters", min = 4, max = 50)
        String state;

        @Size(message = "Country name length should be between 4 and 50", min = 4, max = 50)
        String country;

        @Size(message = "Address line should be more than 4 characters long", min = 4)
        String addressLine;

        @Digits(message = "Zip code should be of 6 digits", integer = 6, fraction = 0)
        Integer zipCode;

        @Size(message = "Label cannot be more than 50 characters", max = 50)
        String label;
}