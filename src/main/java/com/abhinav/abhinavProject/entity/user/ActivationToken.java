package com.abhinav.abhinavProject.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "token"))
public class ActivationToken {

    @Id
    long id;

    String token;

    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    Customer customer;

    LocalDateTime expiration;

}
