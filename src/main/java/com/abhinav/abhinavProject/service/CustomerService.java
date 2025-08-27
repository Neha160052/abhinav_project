package com.abhinav.abhinavProject.service;


import com.abhinav.abhinavProject.co.CustomerProfileUpdateCO;
import com.abhinav.abhinavProject.co.CustomerRegisterCO;
import com.abhinav.abhinavProject.entity.user.Customer;
import com.abhinav.abhinavProject.vo.CustomerDetailsDTO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    Customer registerCustomer(CustomerRegisterCO customerRegisterCO);

    void activateCustomerAccount(String token);

    void resendActivationCode(String email);

    PageResponseVO<List<CustomerDetailsDTO>> getCustomers(String email, Pageable pageable);

    CustomerDetailsDTO getCustomerDetails();

//    void updateSellerDetails(CustomerProfileUpdateCO customerProfileUpdateCO);
}
