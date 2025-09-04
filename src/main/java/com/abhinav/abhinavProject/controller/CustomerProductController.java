package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.service.ProductService;
import com.abhinav.abhinavProject.vo.CustomerProductDetailsVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
