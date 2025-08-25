package com.abhinav.abhinavProject.entity.order;


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
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    Order order;

    int quantity;

    double price;

    @ManyToOne
    ProductVariation productVariation;

}
