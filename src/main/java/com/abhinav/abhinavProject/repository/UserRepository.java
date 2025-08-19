package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Role;
import com.abhinav.abhinavProject.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByRole(Role role);

    boolean existsByEmail(String email);

    User findByEmail(String email);
}