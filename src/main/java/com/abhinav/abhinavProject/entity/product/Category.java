package com.abhinav.abhinavProject.entity.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;

//    @OneToOne(mappedBy = "parentCategory")

//    @ManyToOne
//    @JoinColumn(name = "parent_category_id")
//    Category parentCategory;

}
