package com.abhinav.abhinavProject.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryFieldAndValuesVO {
    String name;
    Set<String> values;
}