package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.category.CategoryMetadataField;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetadataFieldDetailsVO {
    long id;
    String name;

    public MetadataFieldDetailsVO(CategoryMetadataField categoryMetadataField) {
        this.id = categoryMetadataField.getId();
        this.name = categoryMetadataField.getName();
    }
}
