package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.ResetPasswordCO;
import com.abhinav.abhinavProject.entity.user.PasswordResetToken;
import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    EmailServiceImpl emailServiceImpl;
    private final PasswordEncoder passwordEncoder;

    private User generateNewPasswordResetTokenAndSendEmail(User user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();

        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiration(LocalDateTime.now().plusHours(3));

        user.setPasswordResetToken(passwordResetToken);

        User savedUser = userRepository.save(user);

        emailServiceImpl.sendPasswordResetEmail(savedUser.getFirstName(),
                savedUser.getEmail(),
                savedUser.getPasswordResetToken().getToken());

        return savedUser;
    }

    public void sendResetPasswordLink(String email) {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new RuntimeException("Email does not exist");
        }

        if(!user.isActive()) {
            throw new RuntimeException("Account not activated. Please activate before resetting password.");
        }

        if(user.getPasswordResetToken()!=null) {
            emailServiceImpl.sendPasswordResetEmail(user.getFirstName(),
                    user.getEmail(),
                    user.getPasswordResetToken().getToken());
            return;
        }

        generateNewPasswordResetTokenAndSendEmail(user);
        
    }

    public void resetUserPassword(ResetPasswordCO resetPasswordCO, String token) {

        if(!resetPasswordCO.getPassword().equals(resetPasswordCO.getConfirmPassword())) {
            throw new RuntimeException("Password mismatch. Please check password and confirm password.");
        }

        User user = userRepository.findByPasswordResetToken_Token(token);

        if (user == null) {
            throw new RuntimeException("Invalid token provided");
        }

        if (user.getPasswordResetToken()
                .getExpiration()
                .isBefore(LocalDateTime.now())
        ) {
            generateNewPasswordResetTokenAndSendEmail(user);
            throw new RuntimeException("Password Reset code expired. New reset link has been emailed.");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordCO.getPassword()));
        user.setPasswordResetToken(null);

        userRepository.save(user);
    }

}
