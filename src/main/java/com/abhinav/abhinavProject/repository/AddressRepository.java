package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByIdAndUserId(long id1, long id2);
}