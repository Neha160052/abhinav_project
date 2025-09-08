package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.BlacklistTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface BlacklistTokensRepository extends JpaRepository<BlacklistTokens, String> {
    boolean existsByTokenId(String tokenId);

    @Modifying
    @Query("delete from BlacklistTokens b where b.expiringAt < ?1")
    void deleteByExpiringAtBefore(Date now);
}