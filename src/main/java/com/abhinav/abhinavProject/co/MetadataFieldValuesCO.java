package com.abhinav.abhinavProject.co;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetadataFieldValuesCO {
    @NotNull(message = "Metadata field ID cannot be null.")
    long metadataFieldId;

    @NotEmpty(message = "Values cannot be empty.")
    Set<String> values;
}