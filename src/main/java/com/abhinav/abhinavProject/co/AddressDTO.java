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
public class AddressDTO {

        @NotBlank(message = "{address.city.required}")
        @Size(message = "{address.city.size}", min = 4, max = 15)
        String city;

        @NotBlank(message = "{address.state.required}")
        @Size(message = "{address.state.size}", min = 4, max = 50)
        String state;

        @NotBlank(message = "{address.country.required}")
        @Size(message = "{address.country.size}", min = 4, max = 50)
        String country;

        @NotBlank(message = "{address.line.required}")
        @Size(message = "{address.line.size}", min = 4, max = 100)
        String addressLine;

        @NotBlank(message = "{address.zipcode.required}")
        @Pattern(regexp = Regex.ZIPCODE, message = "{address.zipcode.pattern}")
        String zipCode;

        @NotBlank(message = "{address.label.required}")
        @Size(message = "{address.label.size}", min = 2, max = 50)
        String label;
}