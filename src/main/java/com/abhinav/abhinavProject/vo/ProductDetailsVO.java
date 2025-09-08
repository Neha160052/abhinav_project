package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.product.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailsVO {
    Long id;
    String name;
    String brand;
    String description;
    Boolean isCancellable;
    Boolean isReturnable;
    Boolean isActive;
    Boolean isDeleted;
    CategoryDetailsVO category;
    List<ProductVariationDetailsVO> productVariations;

    public ProductDetailsVO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.description = product.getDescription();
        this.isCancellable = product.isCancellable();
        this.isReturnable = product.isReturnable();
        this.category = new CategoryDetailsVO(product.getCategory());
    }
}
