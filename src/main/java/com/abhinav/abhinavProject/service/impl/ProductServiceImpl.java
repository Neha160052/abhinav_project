package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.co.UpdateProductCO;
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
import com.abhinav.abhinavProject.specification.ProductSpecification;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerProductDetailsVO;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Seller seller = getSellerFromContext();

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
        Seller seller = getSellerFromContext();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found."));

        validateOwnership(product, seller);

        return new SellerProductDetailsVO(product);
    }

    @Override
    public PageResponseVO<List<SellerProductDetailsVO>> getAllProducts(Integer page, Integer size, String sort, String order, String query) {
        Seller seller = getSellerFromContext();

        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Specification<Product> spec = ProductSpecification.hasSellerId(seller.getId());

        if (!query.isBlank()) {
            spec = spec.and(ProductSpecification.nameContains(query).or(ProductSpecification.brandContains(query)));
        }

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        List<SellerProductDetailsVO> productDetailsVOS = productPage.getContent().stream()
                .map(SellerProductDetailsVO::new)
                .toList();

        return new PageResponseVO<>(
                productPage.getNumber(),
                productPage.getSize(),
                productPage.hasNext(),
                productDetailsVOS
        );
    }

    @Override
    public void deleteProduct(long id) {
        Seller seller = getSellerFromContext();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        validateOwnership(product, seller);

        productRepository.delete(product);
    }

    @Override
    public void updateProduct(long id, UpdateProductCO updateProductCO) {
        Seller seller = getSellerFromContext();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        validateOwnership(product, seller);

        if (nonNull(updateProductCO.getName())) {
            boolean nameExists = productRepository.existsByNameAndBrandAndSeller_IdAndCategory_Id(
                    updateProductCO.getName(),
                    product.getBrand(),
                    seller.getId(),
                    product.getCategory().getId()
            );

            if (nameExists) {
                throw new ValidationException("Product with this name and brand already exists under the category");
            }
            product.setName(updateProductCO.getName());
        }

        if (nonNull(updateProductCO.getDescription()))
            product.setDescription(updateProductCO.getDescription());
        if (nonNull(updateProductCO.getIsCancellable()))
            product.setCancellable(updateProductCO.getIsCancellable());
        if (nonNull(updateProductCO.getIsReturnable()))
            product.setReturnable(updateProductCO.getIsReturnable());

        productRepository.save(product);
    }

    private Seller getSellerFromContext() {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return sellerRepository.findByUser_Email(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Seller not found"));
    }

    private static void validateOwnership(Product product, Seller seller) {
        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new AccessDeniedException("You are not authorized to access this product");
        }
    }
}
