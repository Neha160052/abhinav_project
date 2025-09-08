package com.abhinav.abhinavProject.co;

import jakarta.validation.constraints.NotBlank;
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
    @NotNull(message = "{metadatafield.id.required}")
    long metadataFieldId;

    @NotEmpty(message = "{metadata.values.required}")
    Set<@NotBlank(message = "{metadata.values.notBlank}") String> values;
}