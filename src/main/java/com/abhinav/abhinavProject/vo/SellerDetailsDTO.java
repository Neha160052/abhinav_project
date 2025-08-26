package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.user.Address;
import com.abhinav.abhinavProject.entity.user.Seller;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.StringJoiner;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerDetailsDTO {

    long id;
    String fullName;
    String email;
    boolean isActive;
    boolean isLocked;
    String companyName;
    long companyContact;
    Address companyAddress;
    String gst;

    public SellerDetailsDTO(Seller seller) {
        String middleName = seller.getUser().getMiddleName();
        this.id = seller.getId();
        this.fullName = new StringJoiner(" ")
                .add(seller.getUser().getFirstName())
                .add(middleName != null ? middleName : "")
                .add(seller.getUser().getLastName())
                .toString();
        this.email = seller.getUser().getEmail();
        this.isActive = seller.getUser().isActive();
        this.isLocked = seller.getUser().isLocked();
        this.companyName = seller.getCompanyName();
        this.companyContact = seller.getCompanyContact();
        this.companyAddress = seller.getUser().getAddress().stream().findFirst().orElse(new Address());
        this.gst = seller.getGst();
    }
}
