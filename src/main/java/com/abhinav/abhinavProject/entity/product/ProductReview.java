package com.abhinav.abhinavProject.entity.product;

import com.abhinav.abhinavProject.constant.RatingEnum;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    Customer customer;

    @ManyToOne
    Product product;

    String review;

    @Enumerated(EnumType.STRING)
    RatingEnum rating;

}
