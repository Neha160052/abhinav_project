package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerProductDetailsVO {
    long id;
    String name;
    String brand;
    String description;
    boolean isCancellable;
    boolean isReturnable;
    boolean isActive;
    CategoryDetailsVO category;

    public SellerProductDetailsVO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.description = product.getDescription();
        this.isCancellable = product.isCancellable();
        this.isReturnable = product.isReturnable();
        this.isActive = product.isActive();
        this.category = new CategoryDetailsVO(product.getCategory());
    }
}
