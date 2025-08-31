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
public class AddressPatchDTO {

        @Size(message = "{address.city.size}", min = 4, max = 15)
        String city;

        @Size(message = "{address.state.size}", min = 4, max = 50)
        String state;

        @Size(message = "{address.country.size}", min = 4, max = 50)
        String country;

        @Size(message = "{address.line.size}", min = 4, max = 100)
        String addressLine;

        @Pattern(regexp = Regex.ZIPCODE, message = "{address.zipcode.pattern}")
        String zipCode;

        @Size(message = "{address.label.size}", min = 2, max = 50)
        String label;
}