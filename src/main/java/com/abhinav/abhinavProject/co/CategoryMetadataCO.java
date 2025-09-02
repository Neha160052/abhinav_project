package com.abhinav.abhinavProject.co;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMetadataCO {

    @NotNull(message = "Category ID cannot be null.")
    long categoryId;

    @Valid
    @NotEmpty(message = "Metadata field values list cannot be empty.")
    List<MetadataFieldValuesCO> metadataFieldValues;
}