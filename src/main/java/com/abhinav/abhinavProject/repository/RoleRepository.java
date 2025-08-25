package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByAuthority(String authority);
}