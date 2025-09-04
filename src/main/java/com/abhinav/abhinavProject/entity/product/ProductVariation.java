package com.abhinav.abhinavProject.entity.product;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    Product product;

    int quantityAvailable;

    double price;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    Map<String, String> metadata = new HashMap<>();

    String primaryImageName;

    boolean isActive;

}
