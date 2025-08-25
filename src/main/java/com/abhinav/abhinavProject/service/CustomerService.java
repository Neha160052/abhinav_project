package com.abhinav.abhinavProject.service;


import com.abhinav.abhinavProject.co.CustomerRegisterCO;
import com.abhinav.abhinavProject.entity.user.Customer;

public interface CustomerService {
    Customer registerCustomer(CustomerRegisterCO customerRegisterCO);

    void activateCustomerAccount(String token);

    void resendActivationCode(String email);
}
