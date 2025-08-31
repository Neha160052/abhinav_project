package com.abhinav.abhinavProject.co;

import com.abhinav.abhinavProject.constant.Regex;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerProfileUpdateCO {
    @Size(min = 3, max = 20, message = "{user.firstname.size}")
    @Pattern(
            regexp = Regex.NAME,
            message = "{user.firstname.pattern}"
    )
    String firstName;

    @Size(min = 3, max = 20, message = "{user.middlename.size}")
    @Pattern(
            regexp = Regex.NAME,
            message = "{user.middlename.pattern}"
    )
    String middleName;

    @Size(min = 3, max = 20, message = "{user.lastname.size}")
    @Pattern(
            regexp = Regex.NAME,
            message = "{user.lastname.pattern}"
    )
    String lastName;

    @Pattern(
            regexp = Regex.CONTACT,
            message = "{user.phone.pattern}"
    )
    String companyContact;

    @Pattern(regexp = Regex.GST, message = "{seller.gst.pattern}")
    String gst;

    @Size(min = 3, max = 20, message = "{seller.companyname.size}")
    String companyName;
}
