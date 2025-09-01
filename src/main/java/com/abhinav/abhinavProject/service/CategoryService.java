package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.NewCategoryCO;
import com.abhinav.abhinavProject.vo.CategoryDetailsVO;

public interface CategoryService {
    CategoryDetailsVO addNewCategory(NewCategoryCO newCategoryCO);
}
