package com.abhinav.abhinavProject.co;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record AddressDTO(
        @Size(message = "City name should be between 4 and 15 characters", min = 4, max = 15)
        @NotBlank(message = "City name cannot be blank")
        @Length(message = "City name should be between 4 and 15 characters", min = 4, max = 15)
        String city,
        @Size(message = "State name length should be between 4 and 50 characters", min = 4, max = 50)
        @NotBlank
        String state,
        @Size(message = "Country name length should be between 4 and 50", min = 4, max = 50)
        @NotBlank(message = "Country name cannot be blank")
        String country,
        @Size(message = "Address line should be more than 4 characters long", min = 4)
        @NotBlank
        String addressLine,
        @Digits(message = "Zip code should be of 6 digits", integer = 6, fraction = 0)
        @Positive
        int zipCode,
        @Size(message = "Label cannot be more than 50 characters", max = 50)
        String label) {
}