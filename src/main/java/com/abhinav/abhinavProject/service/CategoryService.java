package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.CategoryMetadataCO;
import com.abhinav.abhinavProject.co.NewCategoryCO;
import com.abhinav.abhinavProject.co.UpdateCategoryCO;
import com.abhinav.abhinavProject.vo.CategoryDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryDetailsVO addNewCategory(NewCategoryCO newCategoryCO);

    CategoryDetailsVO getCategoryDetails(Long id);

    PageResponseVO<List<CategoryDetailsVO>> getAllCategories(Pageable pageable, String query);

    void updateCategory(Long id, UpdateCategoryCO updateCategoryCO);

    void addCategoryMetadataField(CategoryMetadataCO addCategoryMetadataFieldCO);

    void updateCategoryMetadataField(CategoryMetadataCO categoryMetadataCO);

    PageResponseVO<List<CategoryDetailsVO>> getAllSellerCategories(Pageable pageable);
}
