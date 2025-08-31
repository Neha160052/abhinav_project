package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Page<Seller> findByUser_EmailContainsIgnoreCase(String email, Pageable pageable);
    Optional<Seller> findByUser_Email(String email);
}