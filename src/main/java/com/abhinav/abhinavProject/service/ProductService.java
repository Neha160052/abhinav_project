package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.co.AddProductVariationCO;
import com.abhinav.abhinavProject.co.UpdateProductCO;
import com.abhinav.abhinavProject.co.UpdateProductVariationCO;
import com.abhinav.abhinavProject.filter.ProductVariationFilter;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.ProductDetailsVO;
import com.abhinav.abhinavProject.vo.ProductVariationDetailsVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {

    void addNewProduct(AddProductCO addProductCO);

    void updateProductVariation(long id, UpdateProductVariationCO co, MultipartFile primaryImage, List<MultipartFile> secondaryImages) throws IOException;

    ProductDetailsVO getSellerProduct(long id);

    PageResponseVO<List<ProductDetailsVO>> getAllSellerProducts(String query, Pageable pageable);

    void deleteProduct(long id);

    void updateProduct(long id, UpdateProductCO updateProductCO);

    String activateProduct(long id);

    String deactivateProduct(long id);

    void addProductVariation(long id, AddProductVariationCO addProductVariationCO, MultipartFile primaryImage, List<MultipartFile> secondaryImages) throws IOException;

    ProductVariationDetailsVO getSellerProductVariation(long id);

    PageResponseVO<List<ProductVariationDetailsVO>> getAllSellerProductVariation(Long id, ProductVariationFilter filter, Pageable pageable);

    ProductDetailsVO getCustomerProduct(long id);

    PageResponseVO<List<ProductDetailsVO>> getAllCustomerProducts(long categoryId, String query, Map<String, String> metadataFilters, Pageable pageable);

    ProductDetailsVO getAdminProduct(long id);

    PageResponseVO<List<ProductDetailsVO>> getAllAdminProducts(String query, Long categoryId, Long sellerId, Pageable pageable);

    PageResponseVO<List<ProductDetailsVO>> getSimilarProducts(long id, String query, Pageable pageable);
}
