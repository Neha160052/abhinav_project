package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.category.CategoryMetadataFieldValues;
import com.abhinav.abhinavProject.entity.category.CategoryMetadataFieldValuesCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetadataFieldValuesRepository extends JpaRepository<CategoryMetadataFieldValues, CategoryMetadataFieldValuesCompositeKey> {
}