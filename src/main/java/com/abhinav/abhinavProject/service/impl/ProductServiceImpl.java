package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.co.AddProductVariationCO;
import com.abhinav.abhinavProject.co.UpdateProductCO;
import com.abhinav.abhinavProject.co.UpdateProductVariationCO;
import com.abhinav.abhinavProject.entity.category.Category;
import com.abhinav.abhinavProject.entity.category.CategoryMetadataFieldValues;
import com.abhinav.abhinavProject.entity.product.Product;
import com.abhinav.abhinavProject.entity.product.ProductVariation;
import com.abhinav.abhinavProject.entity.user.Seller;
import com.abhinav.abhinavProject.exception.*;
import com.abhinav.abhinavProject.filter.ProductVariationFilter;
import com.abhinav.abhinavProject.repository.*;
import com.abhinav.abhinavProject.security.UserPrinciple;
import com.abhinav.abhinavProject.service.CategoryService;
import com.abhinav.abhinavProject.service.ImageService;
import com.abhinav.abhinavProject.service.ProductService;
import com.abhinav.abhinavProject.specification.ProductSpecification;
import com.abhinav.abhinavProject.specification.ProductVariationSpecification;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.*;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    SellerRepository sellerRepository;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    EmailServiceImpl emailServiceImpl;
    MessageUtil messageUtil;
    CategoryMetadataFieldValuesRepository fieldValuesRepository;
    ImageService imageService;
    ProductVariationRepository productVariationRepository;
    CategoryService categoryService;

    @Override
    public void addNewProduct(AddProductCO addProductCO) {
        Seller seller = getSellerFromContext();

        Category category = categoryRepository.findById(addProductCO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(messageUtil.getMessage("category.notFound", addProductCO.getCategoryId())));

        if (!categoryRepository.findByParentCategory_Id(category.getId()).isEmpty()) {
            throw new ValidationException(messageUtil.getMessage("category.notLeaf", category.getId()));
        }

        boolean nameExists = productRepository.existsByNameAndBrandAndSeller_IdAndCategory_Id(
                addProductCO.getName(),
                addProductCO.getBrand(),
                seller.getId(),
                addProductCO.getCategoryId()
        );

        if (nameExists) {
            throw new ValidationException(messageUtil.getMessage(
                    "product.name.alreadyExists",
                    addProductCO.getName(),
                    addProductCO.getBrand()
            ));
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
        emailServiceImpl.sendProductAddedEmail(savedProduct);
    }

    @Override
    @Transactional(rollbackFor = {ValidationException.class, IOException.class})
    public void addProductVariation(long id, AddProductVariationCO co, MultipartFile primaryImage, List<MultipartFile> secondaryImages) throws IOException {
        Seller seller = getSellerFromContext();
        Product product = getProductEntity(id);
        validateOwnership(product, seller);
        if (!product.isActive()) {
            throw new ValidationException(messageUtil.getMessage("product.inactive", id));
        }

        validateMetadata(product.getCategory().getId(), co.getMetadata());
        validateMetadataStructureAndUniqueness(product, co.getMetadata());

        ProductVariation variation = new ProductVariation();
        variation.setProduct(product);
        variation.setPrice(co.getPrice());
        variation.setQuantityAvailable(co.getQuantityAvailable());
        variation.setMetadata(co.getMetadata());
        variation.setActive(true);
        ProductVariation savedVariation = productVariationRepository.save(variation);

        String imageName = imageService.saveVariationPrimaryImage(savedVariation, primaryImage);
        imageService.saveVariationSecondaryImages(savedVariation, secondaryImages);
        savedVariation.setPrimaryImageName(imageName);
        productVariationRepository.save(variation);
    }

    @Override
    @Transactional(rollbackFor = {IOException.class, ValidationException.class})
    public void updateProductVariation(long id, UpdateProductVariationCO co, MultipartFile primaryImage, List<MultipartFile> secondaryImages) throws IOException {
        Seller seller = getSellerFromContext();
        ProductVariation variation = getProductVariationEntity(id);
        Product product = variation.getProduct();

        validateOwnership(product, seller);

        if (product.isDeleted()) {
            throw new ProductNotFoundException(messageUtil.getMessage("product.notFound", product.getId()));
        }
        if (!product.isActive()) {
            throw new ValidationException(messageUtil.getMessage("product.inactive", product.getId()));
        }

        if (nonNull(co.getPrice())) {
            variation.setPrice(co.getPrice());
        }
        if (nonNull(co.getQuantityAvailable())) {
            variation.setQuantityAvailable(co.getQuantityAvailable());
        }
        if (nonNull(co.getIsActive())) {
            variation.setActive(co.getIsActive());
        }

        if (nonNull(co.getMetadata()) && !co.getMetadata().isEmpty()) {
            validateMetadata(product.getCategory().getId(), co.getMetadata());
            variation.getMetadata().putAll(co.getMetadata());
        }

        if (nonNull(primaryImage) && !primaryImage.isEmpty()) {
            String imageName = imageService.saveVariationPrimaryImage(variation, primaryImage);
            variation.setPrimaryImageName(imageName);
        }
        if (nonNull(secondaryImages) && !secondaryImages.isEmpty()) {
            imageService.saveVariationSecondaryImages(variation, secondaryImages);
        }

        productVariationRepository.save(variation);
    }

    @Override
    public SellerProductDetailsVO getSellerProduct(long id) {
        Seller seller = getSellerFromContext();
        Product product = getProductEntity(id);

        validateOwnership(product, seller);

        return new SellerProductDetailsVO(product);
    }

    @Override
    public CustomerProductDetailsVO getCustomerProduct(long id) {
        Product product = getProductEntity(id);

        if (!product.isActive()) {
            throw new ProductNotFoundException(messageUtil.getMessage("product.notFound", id));
        }

        List<ProductVariation> activeVariations = product.getVariations().stream()
                .filter(ProductVariation::isActive)
                .toList();

        if (activeVariations.isEmpty()) {
            throw new ValidationException(messageUtil.getMessage("product.variation.noneActive"));
        }

        CustomerProductDetailsVO productDetailsVO = new CustomerProductDetailsVO(product);

        List<CustomerProductVariationDetailsVO> variationVOs = activeVariations.stream()
                .map(this::mapToCustomerProductVariationVO)
                .toList();

        productDetailsVO.setProductVariations(variationVOs);

        return productDetailsVO;
    }

    @Override
    public AdminProductDetailsVO getAdminProduct(long id) {
        Product product = productRepository.findByIdAdmin(id)
                .orElseThrow(()-> new ProductNotFoundException(messageUtil.getMessage("product.notFound", id)));
        return mapToAdminProductDetailsVO(product);
    }

    @Override
    public SellerProductVariationDetailsVO getProductVariation(long id) {
        Seller seller = getSellerFromContext();
        ProductVariation productVar = getProductVariationEntity(id);

        validateOwnership(productVar.getProduct(), seller);
        if (productVar.getProduct().isDeleted()) {
            throw new ProductVariationNotFoundException(messageUtil.getMessage("product.variation.notFound", id));
        }

        return setPrimaryImage(new SellerProductVariationDetailsVO(productVar));
    }

    @Override
    public PageResponseVO<List<SellerProductDetailsVO>> getAllProducts(String query, Pageable pageable) {
        Seller seller = getSellerFromContext();

        Specification<Product> spec = ProductSpecification.hasSellerId(seller.getId());

        if (!query.isBlank()) {
            spec = spec.and(ProductSpecification.nameOrBrandContains(query));
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
    public PageResponseVO<List<CustomerProductDetailsVO>> getAllCustomerProducts(long categoryId, String query, Map<String, String> metadataFilters, Pageable pageable) {
        List<Long> categoryIds = categoryService.getDescendantLeafCategoryIds(categoryId);
        if (categoryIds.isEmpty()) {
            return new PageResponseVO<>(pageable.getPageNumber(), pageable.getPageSize(), false, Collections.emptyList());
        }

        Specification<Product> spec = ProductSpecification.isActive()
                .and(ProductSpecification.hasActiveVariations())
                .and(ProductSpecification.hasCategoryIn(categoryIds));

        if (hasText(query)) {
            spec = spec.and(ProductSpecification.nameOrBrandContains(query));
        }
        if (metadataFilters != null && !metadataFilters.isEmpty()) {
            spec = spec.and(ProductSpecification.hasMetadata(metadataFilters));
        }

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        List<CustomerProductDetailsVO> productDetailsVOS = productPage.getContent().stream()
                .map(this::mapToCustomerProductDetailsVO)
                .toList();

        return new PageResponseVO<>(
                productPage.getNumber(),
                productPage.getSize(),
                productPage.hasNext(),
                productDetailsVOS
        );
    }

    @Override
    public PageResponseVO<List<SellerProductVariationDetailsVO>> getAllProductVariation(Long productId, ProductVariationFilter filter, Pageable pageable) {
        Seller seller = getSellerFromContext();
        Product product = getProductEntity(productId);
        if (product.isDeleted()) {
            throw new ProductVariationNotFoundException(messageUtil.getMessage("product.notFound", productId));
        }

        Specification<ProductVariation> spec = ProductVariationSpecification
                .hasSellerId(seller.getId())
                .and(ProductVariationSpecification.hasProductId(productId));
        if (nonNull(filter.getQuantity()))
            spec = spec.and(ProductVariationSpecification.quantityAvailable(filter.getQuantity()));
        if (nonNull(filter.getPrice()))
            spec = spec.and(ProductVariationSpecification.priceEquals(filter.getPrice()));
        if (nonNull(filter.getIsActive()))
            spec = spec.and(ProductVariationSpecification.isVariationActive(filter.getIsActive()));

        Page<ProductVariation> variationPage = productVariationRepository.findAll(spec, pageable);

        List<SellerProductVariationDetailsVO> variationVOs = variationPage.getContent().stream()
                .map(SellerProductVariationDetailsVO::new)
                .map(this::setPrimaryImage)
                .toList();

        return new PageResponseVO<>(
                variationPage.getNumber(),
                variationPage.getSize(),
                variationPage.hasNext(),
                variationVOs
        );
    }

    @Override
    public void deleteProduct(long id) {
        Seller seller = getSellerFromContext();
        Product product = getProductEntity(id);

        validateOwnership(product, seller);

        productRepository.delete(product);
    }

    @Override
    public void updateProduct(long id, UpdateProductCO updateProductCO) {
        Seller seller = getSellerFromContext();
        Product product = getProductEntity(id);

        validateOwnership(product, seller);

        if (nonNull(updateProductCO.getName())) {
            boolean nameExists = productRepository.existsByNameAndBrandAndSeller_IdAndCategory_Id(
                    updateProductCO.getName(),
                    product.getBrand(),
                    seller.getId(),
                    product.getCategory().getId()
            );

            if (nameExists) {
                throw new ValidationException(messageUtil.getMessage(
                        "product.name.alreadyExists",
                        updateProductCO.getName(),
                        product.getBrand()
                ));
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

    @Override
    public String activateProduct(long id) {
        Product product = getProductEntity(id);

        if(product.isActive()) {
            return messageUtil.getMessage("product.alreadyActive");
        }

        product.setActive(true);
        Product savedProduct = productRepository.save(product);

        emailServiceImpl.sendProductActivatedMail(savedProduct);
        return messageUtil.getMessage("product.activated.success");
    }

    @Override
    public String deactivateProduct(long id) {
        Product product = getProductEntity(id);

        if(!product.isActive()) {
            return messageUtil.getMessage("product.alreadyInactive");
        }

        product.setActive(false);
        Product savedProduct = productRepository.save(product);

        emailServiceImpl.sendProductDeactivatedMail(savedProduct);
        return messageUtil.getMessage("product.deactivated.success");
    }

    private Seller getSellerFromContext() {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return sellerRepository.findByUser_Email(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException(messageUtil.getMessage("user.notfound")));
    }

    private Product getProductEntity(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(messageUtil.getMessage("product.notFound", id)));
    }

    private ProductVariation getProductVariationEntity(long id) {
        return productVariationRepository.findById(id)
                .orElseThrow(() -> new ProductVariationNotFoundException(messageUtil.getMessage("product.variation.notFound", id)));
    }

    private SellerProductVariationDetailsVO setPrimaryImage(SellerProductVariationDetailsVO dto) {
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/static/product/")
                .path(String.valueOf(dto.getProductDetails().getId()))
                .path("/variation/")
                .path(String.valueOf(dto.getId()))
                .toUriString();
        dto.setPrimaryImage(uri);
        return dto;
    }

    private CustomerProductVariationDetailsVO mapToCustomerProductVariationVO(ProductVariation variation) {
        CustomerProductVariationDetailsVO vo = new CustomerProductVariationDetailsVO(variation);
        long productId = variation.getProduct().getId();
        long variationId = variation.getId();

        String primaryImageUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/static/product/")
                .path(String.valueOf(productId))
                .path("/variation/")
                .path(String.valueOf(variationId))
                .toUriString();
        vo.setPrimaryImage(primaryImageUri);
        // TODO set secondary images

        return vo;
    }

    private CustomerProductDetailsVO mapToCustomerProductDetailsVO(Product product) {
        CustomerProductDetailsVO productDetailsVO = new CustomerProductDetailsVO(product);
        List<ProductVariation> activeVariations = product.getVariations().stream()
                .filter(ProductVariation::isActive)
                .toList();

        List<CustomerProductVariationDetailsVO> variationVOs = activeVariations.stream()
                .map(this::mapToCustomerProductVariationVO)
                .toList();

        productDetailsVO.setProductVariations(variationVOs);
        return productDetailsVO;
    }

    private AdminProductDetailsVO mapToAdminProductDetailsVO(Product product) {
        AdminProductDetailsVO vo = new AdminProductDetailsVO(product);
        List<AdminProductVariationDetailsVO> variationVOs = product.getVariations().stream()
                .map(this::mapToAdminProductVariationDetailsVO)
                .toList();
        vo.setProductVariations(variationVOs);
        return vo;
    }

    private AdminProductVariationDetailsVO mapToAdminProductVariationDetailsVO(ProductVariation variation) {
        AdminProductVariationDetailsVO vo = new AdminProductVariationDetailsVO(variation);
        String primaryImageUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/static/product/")
                .path(String.valueOf(variation.getProduct().getId()))
                .path("/variation/")
                .path(String.valueOf(variation.getId()))
                .toUriString();
        vo.setPrimaryImage(primaryImageUri);
        return vo;
    }

    private void validateOwnership(Product product, Seller seller) {
        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new AccessDeniedException(messageUtil.getMessage("access.denied"));
        }
    }

    private void validateMetadata(long categoryId, Map<String, String> inputMetadata) {
        List<CategoryMetadataFieldValues> allowedFields = fieldValuesRepository.findByCategory_Id(categoryId);

        Map<String, Set<String>> allowedMetadata = allowedFields.stream()
                .collect(Collectors.toMap(
                        field -> field.getCategoryMetadataField().getName(),
                        CategoryMetadataFieldValues::getValuesList
                ));


        for (Map.Entry<String, String> entry : inputMetadata.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (!allowedMetadata.containsKey(key)) {
                throw new ValidationException(messageUtil.getMessage("variation.metadata.field.invalid", key));
            }
            if (!allowedMetadata.get(key).contains(value)) {
                throw new ValidationException(messageUtil.getMessage("variation.metadata.value.invalid", value, key));
            }
        }
    }

    private void validateMetadataStructureAndUniqueness(Product product, Map<String, String> metadata) {
        List<ProductVariation> variations = product.getVariations();

        if (!variations.isEmpty()) {
            ProductVariation existingVariation = variations.getFirst();
            Set<String> fieldEntries = existingVariation.getMetadata().keySet();

            if (!fieldEntries.equals(metadata.keySet())) {
                throw new ValidationException(messageUtil.getMessage("variation.metadata.structure.invalid"));
            }

            variations.forEach(variation -> {
                if (variation.getMetadata().equals(metadata)) {
                    throw new ValidationException(messageUtil.getMessage("variation.metadata.notUnique"));
                }
            });
        }
    }
}
