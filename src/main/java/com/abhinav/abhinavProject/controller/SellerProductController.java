package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.AddProductCO;
import com.abhinav.abhinavProject.co.AddProductVariationCO;
import com.abhinav.abhinavProject.co.UpdateProductCO;
import com.abhinav.abhinavProject.co.UpdateProductVariationCO;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.filter.ProductVariationFilter;
import com.abhinav.abhinavProject.service.ProductService;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerProductDetailsVO;
import com.abhinav.abhinavProject.vo.SellerProductVariationDetailsVO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/seller/product")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER')")
public class SellerProductController {

    ProductService productService;

    @PostMapping()
    public ResponseEntity<ApiResponse> addProduct(@RequestBody @Valid AddProductCO addProductCO) {
        productService.addNewProduct(addProductCO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse("Product added successfully")
        );
    }

    @GetMapping()
    public ResponseEntity<PageResponseVO<List<SellerProductDetailsVO>>> getAllProducts(
            @PageableDefault(sort = "id") Pageable pageable,
            @RequestParam(required = false) String query) {
        PageResponseVO<List<SellerProductDetailsVO>> products = productService.getAllProducts(query, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerProductDetailsVO> getProduct(@PathVariable long id) {
        SellerProductDetailsVO product = productService.getSellerProduct(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable long id, @RequestBody @Valid UpdateProductCO updateProductCO) {
        productService.updateProduct(id, updateProductCO);
        return ResponseEntity.ok(
                new ApiResponse("Product updated successfully")
        );
    }

    @PostMapping(value = "/{id}/variation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> addProductVariation(@PathVariable long id,
                                                           @RequestPart("data") @Valid AddProductVariationCO addProductVariationCO,
                                                           @RequestPart("primaryImage") MultipartFile primaryImage,
                                                           @RequestPart(value = "secondaryImages", required = false) List<MultipartFile> secondaryImages
    ) throws IOException {
        productService.addProductVariation(id, addProductVariationCO, primaryImage, secondaryImages);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse("Product variation added successfully")
        );
    }

    @GetMapping("/variation/{id}")
    public ResponseEntity<SellerProductVariationDetailsVO> getProductVariation(@PathVariable long id) {
        SellerProductVariationDetailsVO product = productService.getProductVariation(id);
        return ResponseEntity.ok(product);
    }

    @PatchMapping(value = "/variation/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateProductVariation(
            @PathVariable long id,
            @RequestPart("data") @Valid UpdateProductVariationCO updateProductVariationCO,
            @RequestPart(value = "primaryImage", required = false) MultipartFile primaryImage,
            @RequestPart(value = "secondaryImages", required = false) List<MultipartFile> secondaryImages
    ) throws IOException {
        productService.updateProductVariation(id, updateProductVariationCO, primaryImage, secondaryImages);
        return ResponseEntity.ok(new ApiResponse("Product variation updated successfully"));
    }

    @GetMapping("/{id}/variation")
    public ResponseEntity<PageResponseVO<List<SellerProductVariationDetailsVO>>> getAllProductVariation(
            @PathVariable Long id,
            @PageableDefault(sort = "id") Pageable pageable,
            @ModelAttribute ProductVariationFilter filter
    ) {
        return ResponseEntity.ok(productService.getAllProductVariation(id, filter, pageable));
    }
}
