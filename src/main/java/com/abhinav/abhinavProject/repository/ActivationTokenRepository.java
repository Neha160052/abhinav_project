package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {

    void deleteByExpirationBefore(LocalDateTime now);
}