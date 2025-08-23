package com.abhinav.abhinavProject.repository;

import com.abhinav.abhinavProject.entity.user.BlacklistTokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistTokensRepository extends JpaRepository<BlacklistTokens, String> {
    boolean existsByTokenId(String tokenId);
}