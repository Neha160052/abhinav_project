package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {

    @Modifying
    @Query("delete from ActivationToken a where a.expiration < ?1")
    void deleteByExpirationBefore(LocalDateTime now);
}