package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.co.UpdateProductCO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerProductDetailsVO;

import java.util.List;

public interface ProductService {

    void addNewProduct(AddProductCO addProductCO);

    SellerProductDetailsVO getProduct(long id);

    PageResponseVO<List<SellerProductDetailsVO>> getAllProducts(Integer page, Integer size, String sort, String order, String query);

    void deleteProduct(long id);

    void updateProduct(long id, UpdateProductCO updateProductCO);

    String activateProduct(long id);

}
