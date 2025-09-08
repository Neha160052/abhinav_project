package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.*;
import com.abhinav.abhinavProject.vo.AddressVO;
import com.abhinav.abhinavProject.vo.CustomerDetailsDTO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface CustomerService {
    void registerCustomer(CustomerRegisterCO customerRegisterCO);

    void activateCustomerAccount(String token);

    void resendActivationCode(String email);

    PageResponseVO<List<CustomerDetailsDTO>> getCustomers(String email, Pageable pageable);

    CustomerDetailsDTO getCustomerDetails();

    void updateCustomerDetails(CustomerProfileUpdateCO customerProfileUpdateCO);

    void updateCustomerPassword(ResetPasswordCO resetPasswordCO);

    void updateCustomerAddress(long id, AddressPatchDTO addressPatchDTO);

    Set<AddressVO> getCustomerAddresses();

    void addCustomerAddress(AddressCO addressCO);

    void deleteCustomerAddress(long addressId);

    void addCustomerProfileImage(MultipartFile image);
}
