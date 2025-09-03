package com.abhinav.abhinavProject.entity.user;


import com.abhinav.abhinavProject.entity.product.ProductVariation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    Customer customer;

    @ManyToOne
    ProductVariation productVariation;

    int quantity;

    boolean isWishlistItem;

}
