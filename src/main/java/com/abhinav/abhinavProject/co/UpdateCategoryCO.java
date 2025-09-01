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
public class UpdateCategoryCO {
    @NotBlank(message = "{category.name.required}")
    @Size(min = 3, max = 20, message = "{category.name.size}")
    @Pattern(regexp = Regex.NAME, message = "{category.name.pattern}")
    String categoryName;
}
