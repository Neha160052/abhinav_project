package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.EmailRequestCO;
import com.abhinav.abhinavProject.co.LoginRequestCO;
import com.abhinav.abhinavProject.co.ResetPasswordCO;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.AuthService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.AuthTokenResponseVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    AuthService authService;
    MessageUtil messageUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponseVO> login(@RequestBody @Valid LoginRequestCO loginRequestCO) {
        String[] tokens = authService.loginUser(loginRequestCO);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE,
                        ResponseCookie.from("refreshToken", tokens[1])
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(Duration.ofHours(24))
                                .sameSite("Strict")
                                .build()
                                .toString()
                )
                .body(new AuthTokenResponseVO(tokens[0], "bearer"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthTokenResponseVO> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = Arrays.stream(
                Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(c-> c.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        messageUtil.getMessage("auth.refresh.token.missing"))
                );

        String[] tokens = authService.refreshJwtTokens(refreshToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE,
                        ResponseCookie.from("refreshToken", tokens[1])
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(Duration.ofHours(24))
                                .sameSite("Strict")
                                .build().toString()
                )
                .body(new AuthTokenResponseVO(tokens[0], "bearer"));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        String accessToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        authService.logoutUser(accessToken);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("auth.logout.success")));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPasswordRequest(@Valid @RequestBody EmailRequestCO emailRequestCO) {
        authService.sendResetPasswordLink(emailRequestCO.getEmail());
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("auth.password.reset.link")));
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordCO resetPasswordCO,
                                                     @RequestParam String token) {
        authService.resetUserPassword(resetPasswordCO, token);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("auth.password.reset.success")));
    }

}
