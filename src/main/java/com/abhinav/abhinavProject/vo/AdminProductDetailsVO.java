package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.product.Product;
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
public class AdminProductDetailsVO {
    long id;
    String name;
    String brand;
    String description;
    boolean isCancellable;
    boolean isReturnable;
    boolean isActive;
    boolean isDeleted;
    CategoryDetailsVO category;
    List<AdminProductVariationDetailsVO> productVariations;

    public AdminProductDetailsVO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.description = product.getDescription();
        this.isCancellable = product.isCancellable();
        this.isReturnable = product.isReturnable();
        this.isActive = product.isActive();
        this.isDeleted = product.isDeleted();
        this.category = new CategoryDetailsVO(product.getCategory());
    }
}
