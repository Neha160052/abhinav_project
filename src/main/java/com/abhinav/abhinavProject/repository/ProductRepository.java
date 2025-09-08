package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
  boolean existsByCategoryId(Long parentCategoryId);

  @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.category.id IN :categoryIds")
  List<String> findDistinctBrandsByCategoryIds(List<Long> categoryIds);

  boolean existsByNameAndBrandAndSeller_IdAndCategory_Id(String name, String brand, Long id, long id1);

  @NativeQuery("select * from product where id = ?")
  Optional<Product> findByIdAdmin(long id);
}