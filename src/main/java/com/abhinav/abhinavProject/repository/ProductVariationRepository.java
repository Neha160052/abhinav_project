package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {
}