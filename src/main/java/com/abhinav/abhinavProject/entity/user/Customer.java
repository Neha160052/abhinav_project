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
public class Customer {

    @Id
    Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    User user;

    long contact;

}
