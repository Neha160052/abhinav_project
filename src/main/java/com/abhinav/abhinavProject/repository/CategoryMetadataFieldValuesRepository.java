package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.product.CategoryMetadataFieldValues;
import com.abhinav.abhinavProject.entity.product.CategoryMetadataFieldValuesCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetadataFieldValuesRepository extends JpaRepository<CategoryMetadataFieldValues, CategoryMetadataFieldValuesCompositeKey> {
}