package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndParentCategoryIsNull(String name);

    Optional<Category> findByNameAndParentCategory_Id(String name, long id);
}
