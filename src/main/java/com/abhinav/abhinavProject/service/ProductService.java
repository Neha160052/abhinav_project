package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.co.AddProductVariationCO;
import com.abhinav.abhinavProject.co.UpdateProductCO;
import com.abhinav.abhinavProject.co.UpdateProductVariationCO;
import com.abhinav.abhinavProject.filter.ProductVariationFilter;
import com.abhinav.abhinavProject.vo.CustomerProductDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerProductDetailsVO;
import com.abhinav.abhinavProject.vo.SellerProductVariationDetailsVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    void addNewProduct(AddProductCO addProductCO);

    void updateProductVariation(long id, UpdateProductVariationCO co, MultipartFile primaryImage, List<MultipartFile> secondaryImages) throws IOException;

    SellerProductDetailsVO getSellerProduct(long id);

    PageResponseVO<List<SellerProductDetailsVO>> getAllProducts(String query, Pageable pageable);

    void deleteProduct(long id);

    void updateProduct(long id, UpdateProductCO updateProductCO);

    String activateProduct(long id);

    String deactivateProduct(long id);

    void addProductVariation(long id, AddProductVariationCO addProductVariationCO, MultipartFile primaryImage, List<MultipartFile> secondaryImages) throws IOException;

    SellerProductVariationDetailsVO getProductVariation(long id);

    PageResponseVO<List<SellerProductVariationDetailsVO>> getAllProductVariation(Long id, ProductVariationFilter filter, Pageable pageable);

    CustomerProductDetailsVO getCustomerProduct(long id);
}
