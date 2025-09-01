package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.NewCategoryCO;
import com.abhinav.abhinavProject.entity.category.Category;
import com.abhinav.abhinavProject.entity.category.CategoryMetadataFieldValues;
import com.abhinav.abhinavProject.exception.CategoryNotFoundException;
import com.abhinav.abhinavProject.repository.CategoryMetadataFieldValuesRepository;
import com.abhinav.abhinavProject.repository.CategoryRepository;
import com.abhinav.abhinavProject.repository.ProductRepository;
import com.abhinav.abhinavProject.service.CategoryService;
import com.abhinav.abhinavProject.vo.CategoryDetailsVO;
import com.abhinav.abhinavProject.vo.CategoryFieldAndValuesVO;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMetadataFieldValuesRepository fieldValuesRepository;
    ProductRepository productRepository;

    @Override
    public CategoryDetailsVO addNewCategory(NewCategoryCO newCategoryCO) {
        Category parentCategory = null;

        if (newCategoryCO.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(newCategoryCO.getParentCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Parent category with ID " + newCategoryCO.getParentCategoryId() + " not found."));

             if (productRepository.existsByCategoryId(newCategoryCO.getParentCategoryId())) {
                 throw new ValidationException("Parent category cannot be associated with existing products.");
             }
        }

        validateSiblingNameUniqueness(newCategoryCO.getCategoryName(), parentCategory);

        if (parentCategory != null) {
            validateNameUniquenessAlongPath(newCategoryCO.getCategoryName(), parentCategory);
        }

        Category newCategory = new Category();
        newCategory.setName(newCategoryCO.getCategoryName());
        newCategory.setParentCategory(parentCategory);

        Category savedCategory = categoryRepository.save(newCategory);

        return new CategoryDetailsVO(savedCategory);
    }

    @Override
    public CategoryDetailsVO getCategoryDetails(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        // get list of ancestors
        List<CategoryDetailsVO> parentCategories = getParentCategories(category);

        // get list of children
        List<Category> children = categoryRepository.findByParentCategory_Id(id);
        List<CategoryDetailsVO> childCategories = children.stream().map(CategoryDetailsVO::new).toList();

        // get list of metadata fields and values
        List<CategoryMetadataFieldValues> categoryValues = fieldValuesRepository.findByCategory_Id(id);

        List<CategoryFieldAndValuesVO> metadataVOs = new ArrayList<>();
        for (CategoryMetadataFieldValues metadata : categoryValues) {
            CategoryFieldAndValuesVO vo = new CategoryFieldAndValuesVO();
            vo.setName(metadata.getCategoryMetadataField().getName());
            vo.setValues(metadata.getValuesList());
            metadataVOs.add(vo);
        }

        CategoryDetailsVO detailsVO = new CategoryDetailsVO(category);
        detailsVO.setParentCategoryPath(parentCategories);
        detailsVO.setChildrenCategories(childCategories);
        detailsVO.setFieldAndValues(metadataVOs);

        return detailsVO;
    }

    private void validateSiblingNameUniqueness(String name, Category parentCategory) {
        Optional<Category> existingCategory;
        if (parentCategory == null) {
            existingCategory = categoryRepository.findByNameAndParentCategoryIsNull(name);
        } else {
            existingCategory = categoryRepository.findByNameAndParentCategory_Id(name, parentCategory.getId());
        }

        if (existingCategory.isPresent()) {
            throw new ValidationException("Category name '" + name + "' already exists under this parent.");
        }
    }

    private void validateNameUniquenessAlongPath(String newName, Category parent) {
        Category current = parent;
        while (current != null) {
            if (current.getName().equalsIgnoreCase(newName)) {
                throw new ValidationException(
                    "Category name '" + newName + "' conflicts with an ancestor category's name '" + current.getName() + "'."
                );
            }
            current = current.getParentCategory();
        }
    }

    private List<CategoryDetailsVO> getParentCategories(Category category) {
        List<CategoryDetailsVO> parents = new ArrayList<>();
        Category current = category.getParentCategory();
        while (current != null) {
            parents.add(new CategoryDetailsVO(current));
            current = current.getParentCategory();
        }
        return parents;
    }
}
