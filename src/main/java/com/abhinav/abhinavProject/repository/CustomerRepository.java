package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByActivationToken_Token(String activationTokenToken);

    Customer findByUser_Email(String userEmail);

    Page<Customer> findByUser_EmailContainsIgnoreCase(String email, Pageable pageable);
}