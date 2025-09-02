package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameAndParentCategoryIsNull(String name);

    List<Category> findByParentCategory_Id(long id);

    Optional<Category> findByNameAndParentCategory_Id(String name, long id);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @NativeQuery("select * from category where id NOT IN (select distinct parent_category_id from category where parent_category_id is not null )")
    Page<Category> findLeafCategories(Pageable pageable);
}
