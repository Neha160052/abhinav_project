package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long>, JpaSpecificationExecutor<ProductVariation> {
    @Query("""
       SELECT MIN(v.price), MAX(v.price)
       FROM ProductVariation v
       WHERE v.product.category.id IN :categoryIds
       """)
    Object[][] findPriceRangeByCategoryIds(List<Long> categoryIds);
}