package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Seller;
import com.abhinav.abhinavProject.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}