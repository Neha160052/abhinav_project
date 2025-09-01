package com.abhinav.abhinavProject.entity.category;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMetadataFieldValues {

    @Id
    long id;

    @ManyToOne
    Category category;


    @ManyToOne
    CategoryMetadataField categoryMetadataField;

    //Set<String> valueList;

    String valuesList;

    public void setValuesList(Set<String> valueSet) {
        this.valuesList = valueSet.stream().collect(Collectors.joining(", "));
    }

    public Set<String> getValuesList() {
        return Arrays.stream(valuesList.split(", ")).collect(Collectors.toSet());
    }
}
