package com.abhinav.abhinavProject.co;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCategoryCO {
    @NotBlank(message = "{category.name.required}")
    @Size(min = 3, max = 20, message = "{category.name.size}")
    String categoryName;
    
    @Positive(message = "{category.parentId.digits}")
    Long parentCategoryId;
}
