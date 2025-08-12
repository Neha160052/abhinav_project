package com.abhinav.abhinavProject.entity.product;

import com.abhinav.abhinavProject.entity.user.Customer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReview {

    @EmbeddedId
    ProductReviewCompositeKey key;

    @ManyToOne
    @MapsId("customerId")
    Customer customer;

    @ManyToOne
    @MapsId("productId")
    Product product;

    String review;

    @Enumerated(EnumType.STRING)
    RatingEnum rating;

}
