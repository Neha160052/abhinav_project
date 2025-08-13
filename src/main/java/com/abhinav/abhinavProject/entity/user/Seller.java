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
public class Seller {

    @Id
    @Column(name = "user_id")
    Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    User user;

    String gst;

    long companyContact;

    String companyName;

}
