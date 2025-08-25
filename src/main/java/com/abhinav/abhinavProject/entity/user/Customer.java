package com.abhinav.abhinavProject.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonIgnore
    @Id
    Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    User user;

    long contact;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
            @JsonManagedReference
    ActivationToken activationToken;
}
