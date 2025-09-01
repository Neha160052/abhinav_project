package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.product.Product;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  boolean existsByCategoryId(Long parentCategoryId);
}