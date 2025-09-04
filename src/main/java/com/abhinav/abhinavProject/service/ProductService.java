package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.co.AddProductVariationCO;
import com.abhinav.abhinavProject.co.UpdateProductCO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerProductDetailsVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    void addNewProduct(AddProductCO addProductCO);

    SellerProductDetailsVO getProduct(long id);

    PageResponseVO<List<SellerProductDetailsVO>> getAllProducts(Integer page, Integer size, String sort, String order, String query);

    void deleteProduct(long id);

    void updateProduct(long id, UpdateProductCO updateProductCO);

    String activateProduct(long id);

    String deactivateProduct(long id);

    void addProductVariation(long id, AddProductVariationCO addProductVariationCO, MultipartFile primaryImage, List<MultipartFile> secondaryImages) throws IOException;
}
