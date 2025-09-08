package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.ProductService;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.ProductDetailsVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsVO> viewProduct(@PathVariable long id) {
        ProductDetailsVO productDetails = productService.getAdminProduct(id);
        return ResponseEntity.ok(productDetails);
    }

    @GetMapping
    public ResponseEntity<PageResponseVO<List<ProductDetailsVO>>> viewAllProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long sellerId,
            @PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(productService.getAllAdminProducts(query, categoryId, sellerId, pageable));
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
