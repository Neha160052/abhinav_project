package com.abhinav.abhinavProject.entity.product;

import com.abhinav.abhinavProject.entity.user.Customer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReview {

//    public ProductReview(Product product, Customer customer, String review, RatingEnum rating) {
//        this.key = new ProductReviewCompositeKey(product.getId(), customer.getUser().getId());
//        this.review = review;
//        this.rating = rating;
//    }

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
