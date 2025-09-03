package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.ProductService;
import com.abhinav.abhinavProject.utils.MessageUtil;
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

    @PutMapping("/activate/{id}")
    public ResponseEntity<ApiResponse> activateProduct(@PathVariable long id) {
        String response = productService.activateProduct(id);

        return ResponseEntity.ok(
                new ApiResponse(response)
        );
    }
}
