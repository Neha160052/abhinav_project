package com.abhinav.abhinavProject.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE address SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    User user;

    @ColumnDefault("")
    String city;

    @ColumnDefault("")
    String state;

    @ColumnDefault("")
    String country;

    @ColumnDefault("")
    String addressLine;

    int zipCode;

    @ColumnDefault("Office")
    String label;

    boolean isDeleted;

}
