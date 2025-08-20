package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Customer;
import com.abhinav.abhinavProject.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByActivationToken_Token(String activationTokenToken);
}