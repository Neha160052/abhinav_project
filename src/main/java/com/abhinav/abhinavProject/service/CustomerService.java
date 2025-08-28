package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.*;
import com.abhinav.abhinavProject.entity.user.Address;
import com.abhinav.abhinavProject.entity.user.Customer;
import com.abhinav.abhinavProject.vo.CustomerDetailsDTO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CustomerService {
    Customer registerCustomer(CustomerRegisterCO customerRegisterCO);

    void activateCustomerAccount(String token);

    void resendActivationCode(String email);

    PageResponseVO<List<CustomerDetailsDTO>> getCustomers(String email, Pageable pageable);

    CustomerDetailsDTO getCustomerDetails();

    void updateCustomerDetails(CustomerProfileUpdateCO customerProfileUpdateCO);

    void updateCustomerPassword(ResetPasswordCO resetPasswordCO);

    void updateCustomerAddress(long id, AddressPatchDTO addressPatchDTO);

    Set<Address> getCustomerAddresses();

    void addCustomerAddress(AddressDTO addressDTO);
}
