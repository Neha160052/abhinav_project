package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.entity.category.Category;
import com.abhinav.abhinavProject.entity.product.Product;
import com.abhinav.abhinavProject.entity.user.Seller;
import com.abhinav.abhinavProject.exception.AccessDeniedException;
import com.abhinav.abhinavProject.exception.CategoryNotFoundException;
import com.abhinav.abhinavProject.exception.ProductNotFoundException;
import com.abhinav.abhinavProject.exception.UserNotFoundException;
import com.abhinav.abhinavProject.repository.CategoryRepository;
import com.abhinav.abhinavProject.repository.ProductRepository;
import com.abhinav.abhinavProject.repository.SellerRepository;
import com.abhinav.abhinavProject.security.UserPrinciple;
import com.abhinav.abhinavProject.service.ProductService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.SellerProductDetailsVO;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    SellerRepository sellerRepository;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    EmailServiceImpl emailServiceImpl;
    MessageUtil messageUtil;

    @Override
    public void addNewProduct(AddProductCO addProductCO) {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Seller seller = sellerRepository.findByUser_Email(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Seller not found"));

        Category category = categoryRepository.findById(addProductCO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (!categoryRepository.findByParentCategory_Id(category.getId()).isEmpty()) {
            throw new ValidationException("Provided category is not a leaf category");
        }

        boolean nameExists = productRepository.existsByNameAndBrandAndSeller_IdAndCategory_Id(
                addProductCO.getName(),
                addProductCO.getBrand(),
                seller.getId(),
                addProductCO.getCategoryId()
        );

        if (nameExists) {
            throw new ValidationException("Product with this name and brand already exists under the category");
        }

        Product product = new Product();
        product.setName(addProductCO.getName());
        product.setBrand(addProductCO.getBrand());
        product.setSeller(seller);
        product.setCategory(category);
        if (nonNull(addProductCO.getDescription()))
            product.setDescription(addProductCO.getDescription());
        if (nonNull(addProductCO.getIsCancellable()))
            product.setCancellable(addProductCO.getIsCancellable());
        if (nonNull(addProductCO.getIsReturnable()))
            product.setReturnable(addProductCO.getIsReturnable());

        Product savedProduct = productRepository.save(product);
        emailServiceImpl.sendProductAddEmail(savedProduct);
    }

    @Override
    public SellerProductDetailsVO getProduct(long id) {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Seller seller = sellerRepository.findByUser_Email(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Seller not found"));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found."));

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new AccessDeniedException("You are not authorized to view this product.");
        }

        return new SellerProductDetailsVO(product);
    }
}
