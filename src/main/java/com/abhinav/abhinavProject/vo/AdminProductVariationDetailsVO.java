package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.product.ProductVariation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminProductVariationDetailsVO {
    long id;
    int quantityAvailable;
    double price;
    Map<String, String> metadata;
    String primaryImage;
    boolean isActive;

    public AdminProductVariationDetailsVO(ProductVariation pv) {
        this.id = pv.getId();
        this.quantityAvailable = pv.getQuantityAvailable();
        this.price = pv.getPrice();
        this.metadata = pv.getMetadata();
        this.isActive = pv.isActive();
    }
}