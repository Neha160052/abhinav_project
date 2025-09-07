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
    AddressVO companyAddress;
    String gst;

    public SellerDetailsDTO(Seller seller) {
        String middleName = seller.getUser().getMiddleName();
        StringJoiner joiner = new StringJoiner(" ").add(seller.getUser().getFirstName());
        if(middleName!=null) joiner.add(middleName);
        joiner.add(seller.getUser().getLastName());

        this.id = seller.getId();
        this.fullName = joiner.toString();
        this.email = seller.getUser().getEmail();
        this.isActive = seller.getUser().isActive();
        this.isLocked = seller.getUser().isLocked();
        this.companyName = seller.getCompanyName();
        this.companyContact = seller.getCompanyContact();
        this.companyAddress = new AddressVO(seller.getUser().getAddress().stream().findFirst().orElse(new Address()));
        this.gst = seller.getGst();
    }
}
