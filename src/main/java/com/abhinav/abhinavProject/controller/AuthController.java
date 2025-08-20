package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.EmailRequestCO;
import com.abhinav.abhinavProject.co.ResetPasswordCO;
import com.abhinav.abhinavProject.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    UserService userService;

    @GetMapping("/login")
    public ResponseEntity<String> getString() {
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPasswordRequest(@Valid @RequestBody EmailRequestCO emailRequestCO) {
        userService.sendResetPasswordLink(emailRequestCO.getEmail());
        return ResponseEntity.ok("A reset password link has been sent to the email!");
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordCO resetPasswordCO,
                                                @RequestParam String token) {
        userService.resetUserPassword(resetPasswordCO, token);
        return ResponseEntity.ok("Password had been updated successfully!");
    }

}
