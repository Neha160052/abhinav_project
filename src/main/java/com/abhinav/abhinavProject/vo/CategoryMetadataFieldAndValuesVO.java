package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.category.CategoryMetadataFieldValues;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMetadataFieldAndValuesVO {
    String name;
    Set<String> values;

    public CategoryMetadataFieldAndValuesVO(CategoryMetadataFieldValues values) {
        this.name = values.getCategoryMetadataField().getName();
        this.values = values.getValuesList();
    }
}