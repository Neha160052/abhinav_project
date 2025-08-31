package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByIdAndUser_Id(long id1, long id2);

    boolean existsByIdAndUser_Id(long id, long id1);
}