package com.abhinav.abhinavProject.filter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariationFilter {
    Integer quantity;
    Double price;
    Boolean isActive;
}
