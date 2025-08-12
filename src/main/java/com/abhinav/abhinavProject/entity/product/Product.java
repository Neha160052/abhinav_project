package com.abhinav.abhinavProject.entity.product;

import com.abhinav.abhinavProject.entity.user.Seller;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "seller_user_id")
    Seller seller;

    String name;

    String description;

    boolean isCancellable;

    boolean isReturnable;

    boolean isActive;

    boolean isDeleted;

    @ManyToOne
    Category category;
}
