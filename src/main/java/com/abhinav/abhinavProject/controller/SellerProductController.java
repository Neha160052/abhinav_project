package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.ProductService;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerProductDetailsVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller/product")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SellerProductController {

    ProductService productService;

    @PostMapping()
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductCO addProductCO) {
        productService.addNewProduct(addProductCO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse("Product added successfully")
        );
    }

    @GetMapping()
    public ResponseEntity<PageResponseVO<List<SellerProductDetailsVO>>> getAllProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(required = false) String query) {
        PageResponseVO<List<SellerProductDetailsVO>> products = productService.getAllProducts(page, size, sort, order, query);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerProductDetailsVO> getProduct(@PathVariable long id) {
        SellerProductDetailsVO product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
