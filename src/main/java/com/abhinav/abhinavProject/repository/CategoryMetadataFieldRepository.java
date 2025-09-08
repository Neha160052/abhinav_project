package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.category.CategoryMetadataField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetadataFieldRepository extends JpaRepository<CategoryMetadataField, Long> {
    boolean existsByNameIgnoreCase(String name);
    Page<CategoryMetadataField> findByNameContainingIgnoreCase(String query, Pageable pageable);
}