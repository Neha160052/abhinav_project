package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.NewCategoryCO;
import com.abhinav.abhinavProject.co.UpdateCategoryCO;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.CategoryService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.CategoryDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    CategoryService categoryService;
    MessageUtil messageUtil;

    @GetMapping()
    public ResponseEntity<PageResponseVO<List<CategoryDetailsVO>>> getAllCategories(
            @PageableDefault(sort = "id") Pageable pageable,
            @RequestParam(defaultValue = "") String query
    ) {
        PageResponseVO<List<CategoryDetailsVO>> allCategories = categoryService.getAllCategories(pageable, query.trim());
        return ResponseEntity.ok(allCategories);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> addNewCategory(@RequestBody @Valid NewCategoryCO newCategoryCO) {
        CategoryDetailsVO newCategory = categoryService.addNewCategory(newCategoryCO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(HttpStatus.CREATED.value(),messageUtil.getMessage("category.added.success"), newCategory)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailsVO> getCategoryDetails(@PathVariable Long id) {
        CategoryDetailsVO categoryDetails = categoryService.getCategoryDetails(id);
        return ResponseEntity.ok(categoryDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody @Valid UpdateCategoryCO updateCategoryCO) {
        categoryService.updateCategory(id, updateCategoryCO);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("category.update.success")));
    }
}
