package com.abhinav.abhinavProject.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String city;

    String state;

    String country;

    String addressLine;

    int zipCode;

    String label;

    @ManyToOne
    User user;

}
