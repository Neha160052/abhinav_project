package com.abhinav.abhinavProject.service;


import com.abhinav.abhinavProject.co.CustomerRegisterCO;
import com.abhinav.abhinavProject.entity.user.Customer;
import jakarta.validation.Valid;

public interface CustomerService {
    Customer registerCustomer(CustomerRegisterCO customerRegisterCO);
}
