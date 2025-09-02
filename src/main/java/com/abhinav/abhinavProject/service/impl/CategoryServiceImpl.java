package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.CategoryMetadataCO;
import com.abhinav.abhinavProject.co.MetadataFieldValuesCO;
import com.abhinav.abhinavProject.co.NewCategoryCO;
import com.abhinav.abhinavProject.co.UpdateCategoryCO;
import com.abhinav.abhinavProject.entity.category.Category;
import com.abhinav.abhinavProject.entity.category.CategoryMetadataField;
import com.abhinav.abhinavProject.entity.category.CategoryMetadataFieldValues;
import com.abhinav.abhinavProject.exception.CategoryNotFoundException;
import com.abhinav.abhinavProject.exception.MetadataFieldNotFoundException;
import com.abhinav.abhinavProject.repository.CategoryMetadataFieldRepository;
import com.abhinav.abhinavProject.repository.CategoryMetadataFieldValuesRepository;
import com.abhinav.abhinavProject.repository.CategoryRepository;
import com.abhinav.abhinavProject.repository.ProductRepository;
import com.abhinav.abhinavProject.service.CategoryService;
import com.abhinav.abhinavProject.vo.CategoryDetailsVO;
import com.abhinav.abhinavProject.vo.CategoryMetadataFieldAndValuesVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMetadataFieldRepository categoryMetadataFieldRepository;
    CategoryMetadataFieldValuesRepository fieldValuesRepository;
    ProductRepository productRepository;

    @Override
    public CategoryDetailsVO addNewCategory(NewCategoryCO newCategoryCO) {
        Category parentCategory = null;

        if (newCategoryCO.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(newCategoryCO.getParentCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Parent category with ID " + newCategoryCO.getParentCategoryId() + " not found."));

            if (!fieldValuesRepository.findByCategory_Id(parentCategory.getId()).isEmpty()) {
                throw new ValidationException("Cannot add new category under the parent category. Parent category already has metadata field values associated to it.");
            }

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
        return buildCategoryDetailsVO(category);
    }

    @Override
    public PageResponseVO<List<CategoryDetailsVO>> getAllCategories(Pageable pageable, String query) {
        Page<Category> resultSet = categoryRepository.findByNameContainingIgnoreCase(query, pageable);

        List<CategoryDetailsVO> categories = resultSet.getContent().stream().map(this::buildCategoryDetailsVO).toList();

        return new PageResponseVO<>(
                resultSet.getNumber(),
                resultSet.getSize(),
                resultSet.hasNext(),
                categories
        );
    }

    @Override
    public void updateCategory(Long id, UpdateCategoryCO updateCategoryCO) {
        Category thisCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Category parentCategory = thisCategory.getParentCategory();

        validateSiblingNameUniqueness(updateCategoryCO.getCategoryName(), parentCategory);

        if (parentCategory != null) {
            validateNameUniquenessAlongPath(updateCategoryCO.getCategoryName(), parentCategory);
        }
        thisCategory.setName(updateCategoryCO.getCategoryName());
        categoryRepository.save(thisCategory);
    }

    @Override
    public void addCategoryMetadataField(CategoryMetadataCO addCO) {
        Category category = categoryRepository.findById(addCO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + addCO.getCategoryId() + " not found."));

        if (!categoryRepository.findByParentCategory_Id(category.getId()).isEmpty()) {
            throw new ValidationException("Metadata Fields & Values cannot be added to an intermediate category");
        }

        for (MetadataFieldValuesCO fieldValuesCO : addCO.getMetadataFieldValues()) {
            CategoryMetadataField metadataField = categoryMetadataFieldRepository.findById(fieldValuesCO.getMetadataFieldId())
                    .orElseThrow(() -> new MetadataFieldNotFoundException("Field not found"));

            if (fieldValuesCO.getValues().isEmpty()) {
                throw new ValidationException("Values list for metadata field ID " + fieldValuesCO.getMetadataFieldId() + " cannot be empty.");
            }

            Optional<CategoryMetadataFieldValues> association = fieldValuesRepository.findByCategory_IdAndCategoryMetadataField_Id(category.getId(), metadataField.getId());
            if (association.isPresent()) {
                throw new ValidationException("Metadata field ID " + metadataField.getId() + " is already associated with this category.");
            }

            CategoryMetadataFieldValues newFieldValue = new CategoryMetadataFieldValues();
            newFieldValue.setCategory(category);
            newFieldValue.setCategoryMetadataField(metadataField);
            newFieldValue.setValuesList(fieldValuesCO.getValues());

            fieldValuesRepository.save(newFieldValue);
        }
    }

    @Override
    public void updateCategoryMetadataField(CategoryMetadataCO categoryMetadataCO) {
        Category category = categoryRepository.findById(categoryMetadataCO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryMetadataCO.getCategoryId() + " not found."));

        for (MetadataFieldValuesCO fieldValuesCO : categoryMetadataCO.getMetadataFieldValues()) {
            categoryMetadataFieldRepository.findById(fieldValuesCO.getMetadataFieldId())
                    .orElseThrow(() -> new MetadataFieldNotFoundException("Field not found with id: " + fieldValuesCO.getMetadataFieldId()));

            CategoryMetadataFieldValues association = fieldValuesRepository
                    .findByCategory_IdAndCategoryMetadataField_Id(category.getId(), fieldValuesCO.getMetadataFieldId())
                    .orElseThrow(() -> new ValidationException("Metadata field ID " + fieldValuesCO.getMetadataFieldId() + " is not associated with category ID " + category.getId()));

            if (fieldValuesCO.getValues() == null || fieldValuesCO.getValues().isEmpty()) {
                throw new ValidationException("Values list for metadata field ID " + fieldValuesCO.getMetadataFieldId() + " cannot be null or empty.");
            }

            Set<String> existingValues = association.getValuesList();
            Set<String> combinedValues = new HashSet<>(existingValues);
            combinedValues.addAll(fieldValuesCO.getValues());

            if (combinedValues.size() > existingValues.size()) {
                association.setValuesList(combinedValues);
                fieldValuesRepository.save(association);
            }
        }
    }

    private CategoryDetailsVO buildCategoryDetailsVO(Category category) {
        // get list of ancestors
        List<CategoryDetailsVO> parentCategories = getParentCategories(category);

        // get list of children
        List<Category> children = categoryRepository.findByParentCategory_Id(category.getId());
        List<CategoryDetailsVO> childCategories = children.stream().map(CategoryDetailsVO::new).toList();

        // get list of metadata fields and values
        List<CategoryMetadataFieldValues> categoryFieldValues = fieldValuesRepository.findByCategory_Id(category.getId());

        List<CategoryMetadataFieldAndValuesVO> metadataVOs = categoryFieldValues
                .stream()
                .map(CategoryMetadataFieldAndValuesVO::new)
                .toList();

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
