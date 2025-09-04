package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.ProductService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.AdminProductDetailsVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    ProductService productService;
    MessageUtil messageUtil;

    @GetMapping("/{id}")
    public ResponseEntity<AdminProductDetailsVO> viewProduct(@PathVariable long id) {
        AdminProductDetailsVO productDetails = productService.getAdminProduct(id);
        return ResponseEntity.ok(productDetails);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<ApiResponse> activateProduct(@PathVariable long id) {
        String response = productService.activateProduct(id);

        return ResponseEntity.ok(
                new ApiResponse(response)
        );
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivateProduct(@PathVariable long id) {
        String response = productService.deactivateProduct(id);

        return ResponseEntity.ok(
                new ApiResponse(response)
        );
    }
}
