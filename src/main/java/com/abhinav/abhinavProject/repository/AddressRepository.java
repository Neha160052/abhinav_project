package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}