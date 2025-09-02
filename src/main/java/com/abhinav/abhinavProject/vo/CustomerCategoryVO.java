package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.category.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerCategoryVO {
    Long id;
    String name;
    List<CustomerCategoryVO> childCategories;

    public CustomerCategoryVO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
