package com.abhinav.abhinavProject.vo;

import com.abhinav.abhinavProject.entity.user.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressVO {

    Long id;

    String city;

    String state;

    String country;

    String addressLine;

    String zipCode;

    String label;

    public AddressVO(Address address) {
        this.id = address.getId();
        this.city = address.getCity();
        this.state = address.getState();
        this.country = address.getCountry();
        this.addressLine = address.getAddressLine();
        this.zipCode = String.valueOf(address.getZipCode());
        this.label = address.getLabel();
    }

}
