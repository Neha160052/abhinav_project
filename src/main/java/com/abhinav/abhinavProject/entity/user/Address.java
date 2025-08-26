package com.abhinav.abhinavProject.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ColumnDefault("")
    String city;

    @ColumnDefault("")
    String state;

    @ColumnDefault("")
    String country;

    @ColumnDefault("")
    String addressLine;

    @ColumnDefault("000000")
    int zipCode;

    @ColumnDefault("Office")
    String label;

}
