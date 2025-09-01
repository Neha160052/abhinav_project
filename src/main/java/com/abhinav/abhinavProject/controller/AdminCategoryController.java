package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.NewCategoryCO;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.CategoryService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.CategoryDetailsVO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/category")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    CategoryService categoryService;
    MessageUtil messageUtil;

    @PostMapping()
    public ResponseEntity<ApiResponse> addNewCategory(@RequestBody @Valid NewCategoryCO newCategoryCO) {
        CategoryDetailsVO newCategory = categoryService.addNewCategory(newCategoryCO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(HttpStatus.CREATED.value(),messageUtil.getMessage("category.added.success"), newCategory)
        );
    }
}
