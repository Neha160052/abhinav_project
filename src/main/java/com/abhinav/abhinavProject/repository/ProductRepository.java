package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  boolean existsByCategoryId(Long parentCategoryId);

  boolean existsByNameAndBrandAndSeller_IdAndCategory_Id(String name, String brand, Long id, long id1);

}