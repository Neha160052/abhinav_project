package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.ProductService;
import com.abhinav.abhinavProject.vo.SellerProductDetailsVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<SellerProductDetailsVO> getProduct(@PathVariable long id) {
        SellerProductDetailsVO product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }
}
