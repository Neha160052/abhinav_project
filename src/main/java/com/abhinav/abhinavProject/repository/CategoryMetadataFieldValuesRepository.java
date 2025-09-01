package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.category.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryMetadataFieldValuesRepository extends JpaRepository<CategoryMetadataFieldValues, Long> {
    List<CategoryMetadataFieldValues> findByCategory_Id(long id);
}