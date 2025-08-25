package com.abhinav.abhinavProject.entity.order;


import com.abhinav.abhinavProject.constant.PaymentMethodEnum;
import com.abhinav.abhinavProject.entity.AuditData;
import com.abhinav.abhinavProject.entity.user.Customer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    Customer customer;

    double amountPaid;

    @Embedded
    AuditData auditData;

    @Enumerated(EnumType.STRING)
    PaymentMethodEnum paymentMethod;

    String customerAddressCity;
    String customerAddressState;
    String customerAddressCountry;
    String customerAddressLine;
    String customerZipCode;
    String customerLabel;


}
