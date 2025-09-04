package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.service.ProductService;
import com.abhinav.abhinavProject.vo.CustomerProductDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer/product")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerProductController {

    ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerProductDetailsVO> getProductById(@PathVariable long id) {
        CustomerProductDetailsVO productDetails = productService.getCustomerProduct(id);
        return ResponseEntity.ok(productDetails);
    }

    @GetMapping
    public ResponseEntity<PageResponseVO<List<CustomerProductDetailsVO>>> getAllProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) String query,
            @RequestParam Map<String, String> allParams,
            @PageableDefault(sort = "id") Pageable pageable) {
        allParams.remove("categoryId");
        allParams.remove("sellerId");
        allParams.remove("query");
        allParams.remove("page");
        allParams.remove("size");
        allParams.remove("sort");

        PageResponseVO<List<CustomerProductDetailsVO>> products = productService.getAllCustomerProducts(
                categoryId, sellerId, query, allParams, pageable);
        return ResponseEntity.ok(products);
    }

}
