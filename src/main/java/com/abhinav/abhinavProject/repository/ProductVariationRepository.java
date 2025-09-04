package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long>, JpaSpecificationExecutor<ProductVariation> {
}