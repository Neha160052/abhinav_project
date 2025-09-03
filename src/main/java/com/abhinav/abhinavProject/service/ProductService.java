package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.vo.SellerProductDetailsVO;

public interface ProductService {
    void addNewProduct(AddProductCO addProductCO);
    SellerProductDetailsVO getProduct(long id);
}
