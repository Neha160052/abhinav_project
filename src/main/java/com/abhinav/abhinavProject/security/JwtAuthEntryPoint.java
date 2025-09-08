package com.abhinav.abhinavProject.security;

import com.abhinav.abhinavProject.exception.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.info("AuthenticationException Handled in JwtAuthEntryPoint. {}", authException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");


        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.UNAUTHORIZED.value(),
                authException.getMessage(),
                "User could not be authenticated");

        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }
}

