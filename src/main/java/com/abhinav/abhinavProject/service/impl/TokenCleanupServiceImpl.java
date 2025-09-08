package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.repository.ActivationTokenRepository;
import com.abhinav.abhinavProject.repository.BlacklistTokensRepository;
import com.abhinav.abhinavProject.repository.PasswordResetTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TokenCleanupServiceImpl {

    ActivationTokenRepository activationTokenRepository;
    PasswordResetTokenRepository passwordResetTokenRepository;
    BlacklistTokensRepository blacklistTokenRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void purgeExpiredTokens() {
        log.info("Starting expired token cleanup task");

        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        activationTokenRepository.deleteByExpirationBefore(nowLocalDateTime);
        log.info("Cleared expired activation tokens.");

        passwordResetTokenRepository.deleteByExpirationBefore(nowLocalDateTime);
        log.info("Cleared expired password reset tokens.");

        Date nowDate = new Date();
        blacklistTokenRepository.deleteByExpiringAtBefore(nowDate);
        log.info("Cleared expired blacklist tokens.");

        log.info("Expired token cleanup task finished.");
    }
}