package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller/product")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SellerProductController {

    ProductService productService;

    @PostMapping()
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductCO addProductCO) {
        productService.addNewProduct(addProductCO);
        return ResponseEntity.ok(
                new ApiResponse("Product added successfully")
        );
    }
}
