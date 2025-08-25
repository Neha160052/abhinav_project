package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.user.Customer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.StringJoiner;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDetailsDTO {

    long id;
    String fullName;
    String email;
    long contact;
    boolean isActive;
    boolean isLocked;

    public CustomerDetailsDTO(Customer customer){
        String middleName = customer.getUser().getMiddleName();
        this.id = customer.getId();
        this.fullName = new StringJoiner(" ")
                .add(customer.getUser().getFirstName())
                .add(middleName != null ? middleName : "")
                .add(customer.getUser().getLastName())
                .toString();
        this.email = customer.getUser().getEmail();
        this.contact = customer.getContact();
        this.isActive = customer.getUser().isActive();
        this.isLocked = customer.getUser().isLocked();
    }
}
