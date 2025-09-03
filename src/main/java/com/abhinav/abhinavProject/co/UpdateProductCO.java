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
public class UpdateProductCO {

    @Pattern(regexp = Regex.NAME, message = "{product.name.pattern}")
    @Size(min = 3, max = 20, message = "{product.name.size}")
    String name;

    @Size(min = 10, max = 100, message = "{product.desc.size}")
    String description;

    Boolean isCancellable;

    Boolean isReturnable;
}
