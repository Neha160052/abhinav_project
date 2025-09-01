package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.NewCategoryCO;
import com.abhinav.abhinavProject.vo.CategoryDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryDetailsVO addNewCategory(NewCategoryCO newCategoryCO);

    CategoryDetailsVO getCategoryDetails(Long id);
    PageResponseVO<List<CategoryDetailsVO>> getAllCategories(Pageable pageable, String query);
}
