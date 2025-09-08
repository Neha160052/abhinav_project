package com.abhinav.abhinavProject.co;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddProductVariationCO {

    @NotNull(message = "{quantity.required}")
    @PositiveOrZero
    int quantityAvailable;

    @NotNull(message = "{price.required}")
    @PositiveOrZero
    @Digits(integer = 9, fraction = 2, message = "{price.digits}")
    Double price;

    @NotNull(message = "{metadata.required}")
    @NotEmpty(message = "{metadata.empty}")
    Map<String, String> metadata;
}
