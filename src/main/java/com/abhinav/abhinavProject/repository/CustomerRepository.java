package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByActivationToken_Token(String activationTokenToken);

    Optional<Customer> findByUser_Email(String userEmail);

    Page<Customer> findByUser_EmailContainsIgnoreCase(String email, Pageable pageable);
}