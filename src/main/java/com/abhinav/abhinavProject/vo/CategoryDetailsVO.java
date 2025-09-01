package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.category.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDetailsVO {
    long id;
    String name;

    public CategoryDetailsVO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
