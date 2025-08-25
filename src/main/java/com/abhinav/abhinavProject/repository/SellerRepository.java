package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Page<Seller> findByUser_EmailContainsIgnoreCase(String email, Pageable pageable);
}