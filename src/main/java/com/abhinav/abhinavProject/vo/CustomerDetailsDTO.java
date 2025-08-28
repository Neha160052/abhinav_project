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
        StringJoiner joiner = new StringJoiner(" ").add(customer.getUser().getFirstName());
        if(middleName!=null) joiner.add(middleName);
        joiner.add(customer.getUser().getLastName());

        this.id = customer.getId();
        this.fullName = joiner.toString();
        this.email = customer.getUser().getEmail();
        this.contact = customer.getContact();
        this.isActive = customer.getUser().isActive();
        this.isLocked = customer.getUser().isLocked();
    }
}
