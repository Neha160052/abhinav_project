package com.abhinav.abhinavProject.co;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductVariationCO {

    @PositiveOrZero
    Integer quantityAvailable;

    @PositiveOrZero
    Double price;

    Map<String, String> metadata;

    Boolean isActive;
}

