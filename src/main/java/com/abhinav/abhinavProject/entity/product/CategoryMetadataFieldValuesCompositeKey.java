package com.abhinav.abhinavProject.entity.product;


import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMetadataFieldValuesCompositeKey {

    long categoryMetadataFieldId;

    long categoryId;
}
