package com.abhinav.abhinavProject.entity.product;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMetadataFieldValues {

    @EmbeddedId
    CategoryMetadataFieldValuesCompositeKey key;


    @ManyToOne
    @MapsId("categoryId")
    Category category;


    @ManyToOne
    @MapsId("categoryMetadataFieldId")
    CategoryMetadataField categoryMetadataField;

    List<String> values;
}
