package com.abhinav.abhinavProject.security;

import com.abhinav.abhinavProject.repository.BlacklistTokensRepository;
import com.abhinav.abhinavProject.utils.JwtService;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@NonNullApi
public class JwtSecurityFilter extends OncePerRequestFilter {

    JwtService jwtService;
    BlacklistTokensRepository blacklistTokensRepository;
    UserDetailsService userDetailsService;
    AntPathMatcher pathMatcher = new AntPathMatcher();
    AuthenticationEntryPoint authenticationEntryPoint;

    static String[] PUBLIC_ENDPOINTS = {
            "/customer/register",
            "/customer/activate",
            "/customer/activate/resend",
            "/seller/register",
            "/auth/login",
            "/auth/forgot-password",
            "/auth/forgot-password/reset",
            "/auth/refresh-token"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return Arrays.stream(PUBLIC_ENDPOINTS)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
                username = jwtService.extractUserName(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String refreshTokenJti = jwtService.extractRefreshTokenJti(token);

                if (jwtService.validateToken(token, userDetails) &&
                        !blacklistTokensRepository.existsByTokenId(refreshTokenJti)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (JwtException ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response,
                    new InsufficientAuthenticationException("Invalid JWT: " + ex.getMessage(), ex));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
