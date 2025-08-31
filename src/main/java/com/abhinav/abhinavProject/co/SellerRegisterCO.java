package com.abhinav.abhinavProject.co;

import com.abhinav.abhinavProject.constant.Regex;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerRegisterCO {

    @NotBlank(message = "{user.firstname.required}")
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

    @NotBlank(message = "{user.lastname.required}")
    @Size(min = 3, max = 20, message = "{user.lastname.size}")
    @Pattern(
            regexp = Regex.NAME,
            message = "{user.lastname.pattern}"
    )
    String lastName;

    @NotBlank(message = "{email.required}")
    @Email(message = "{email.invalid}")
    String email;

    @NotBlank(message = "{user.phone.required}")
    @Pattern(
            regexp = Regex.CONTACT,
            message = "{user.phone.pattern}"
    )
    String companyContact;

    @NotBlank(message = "{password.required}")
    @Size(min = 8, max = 15, message = "{password.size}")
    @Pattern(
            regexp = Regex.PASSWORD,
            message = "{password.pattern}"
    )
    String password;

    @NotBlank(message = "{user.confirmpassword.required}")
    @Size(min = 8, max = 15, message = "{user.confirmpassword.size}")
    String confirmPassword;

    @NotBlank
    @Pattern(regexp = Regex.GST, message = "{seller.gst.pattern}")
    String gst;

    @NotBlank(message = "{seller.companyname.required}")
    @Size(min = 3, max = 20, message = "{seller.companyname.size}")
    String companyName;

    @Valid
    AddressDTO companyAddress;
}
