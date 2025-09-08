package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
    @Modifying
    @Query("delete from PasswordResetToken p where p.expiration < ?1")
    void deleteByExpirationBefore(LocalDateTime now);
}