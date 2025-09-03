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
public class AddProductCO {

    @NotBlank(message = "{product.name.required}")
    @Pattern(regexp = Regex.NAME, message = "{product.name.pattern}")
    @Size(min = 3, max = 20, message = "{product.name.size}")
    String name;


    @NotBlank(message = "{product.brand.required}")
    @Pattern(regexp = Regex.NAME, message = "{product.brand.pattern}")
    @Size(min = 3, max = 20, message = "{product.brand.size}")
    String brand;

    long categoryId;

    @Size(min = 10, max = 100, message = "{product.desc.size}")
    String description;

    Boolean isCancellable;

    Boolean isReturnable;
}
