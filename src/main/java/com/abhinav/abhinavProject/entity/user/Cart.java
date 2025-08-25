package com.abhinav.abhinavProject.entity.user;


import com.abhinav.abhinavProject.entity.product.ProductVariation;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {

    @EmbeddedId
    CartCompositeKey cartCompositeKey;

    @ManyToOne
    @MapsId("customerId")
    Customer customer;

    @ManyToOne
    @MapsId("productVariationId")
    ProductVariation productVariation;

    int quantity;

    boolean isWishlistItem;

}
