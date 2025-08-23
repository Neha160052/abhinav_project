package com.abhinav.abhinavProject.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {

    @Value("${jwt.secret-key}")
    String JWT_SECRET;

    public String[] generateAccessAndRefreshToken(String username, GrantedAuthority grantedAuthority) {
        String refreshTokenJti = UUID.randomUUID().toString();

        log.info("REFRESH_JTI: {}", refreshTokenJti);

        String refreshToken = Jwts.builder()
                .id(refreshTokenJti)
                .claim("role", grantedAuthority.getAuthority())
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(24L*60*60)))
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
        log.info("REFRESH_TOKEN: {}", refreshToken);

        String accessToken = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claim("role", grantedAuthority.getAuthority())
                .claim("refreshTokenJti", refreshTokenJti)
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(15L*60)))
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
        log.info("ACCESS_TOKEN: {}", accessToken);

        return new String[]{accessToken, refreshToken};
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractGrantedAuthority(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String extractTokenId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public String extractRefreshTokenJti(String token) {
        return extractClaim(token, claims -> claims.get("refreshTokenJti", String.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(getKey()).build().parseClaimsJws(token).getPayload();
        } catch (Exception e) {
            log.error("JWT parsing failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
