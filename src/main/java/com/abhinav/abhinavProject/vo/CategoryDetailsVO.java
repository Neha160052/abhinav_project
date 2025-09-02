package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.category.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDetailsVO {
    long id;
    String name;
    List<CategoryDetailsVO> parentCategoryPath;
    List<CategoryDetailsVO> childrenCategories;
    List<CategoryMetadataFieldAndValuesVO> fieldAndValues;

    public CategoryDetailsVO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
