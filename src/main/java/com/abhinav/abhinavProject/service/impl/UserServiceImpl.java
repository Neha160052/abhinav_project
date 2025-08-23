package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.LoginRequestCO;
import com.abhinav.abhinavProject.co.ResetPasswordCO;
import com.abhinav.abhinavProject.entity.user.BlacklistTokens;
import com.abhinav.abhinavProject.entity.user.PasswordResetToken;
import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.BlacklistTokensRepository;
import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.security.UserPrinciple;
import com.abhinav.abhinavProject.service.UserService;
import com.abhinav.abhinavProject.utils.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    EmailServiceImpl emailServiceImpl;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    BlacklistTokensRepository blacklistTokensRepository;

    public String[] loginUser(LoginRequestCO loginRequestCO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestCO.getEmail(), loginRequestCO.getPassword()));

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        return jwtService.generateAccessAndRefreshToken(userPrinciple.getUsername(),
                userPrinciple.getAuthorities().stream().findFirst().get()
        );
    }

    public void logoutUser(String token) {
        String refreshTokenJti = jwtService.extractRefreshTokenJti(token);
        Date expiry = jwtService.extractExpiration(token);

        BlacklistTokens blacklistToken = new BlacklistTokens(refreshTokenJti, expiry);

        blacklistTokensRepository.save(blacklistToken);
    }

    private void generateNewPasswordResetTokenAndSendEmail(User user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();

        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiration(LocalDateTime.now().plusHours(3));

        user.setPasswordResetToken(passwordResetToken);

        User savedUser = userRepository.save(user);

        emailServiceImpl.sendPasswordResetEmail(savedUser.getFirstName(),
                savedUser.getEmail(),
                savedUser.getPasswordResetToken().getToken());
    }

    public void sendResetPasswordLink(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email does not exist"));

        if (!user.isActive()) {
            throw new RuntimeException("Account not activated. Please activate before resetting password.");
        }

        if (user.getPasswordResetToken() != null) {
            emailServiceImpl.sendPasswordResetEmail(user.getFirstName(),
                    user.getEmail(),
                    user.getPasswordResetToken().getToken());
            return;
        }

        generateNewPasswordResetTokenAndSendEmail(user);

    }

    public void resetUserPassword(ResetPasswordCO resetPasswordCO, String token) {

        if (!resetPasswordCO.getPassword().equals(resetPasswordCO.getConfirmPassword())) {
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

    public String[] refreshJwtTokens(String refreshToken) {
        // Check if refresh token is expired
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh Token is Expired");
        }

        // extract refresh token id and its expiry
        String refreshTokenJti = jwtService.extractTokenId(refreshToken);
        Date refreshTokenExpiration = jwtService.extractExpiration(refreshToken);

        // check if the refresh token is blacklisted
        if (blacklistTokensRepository.existsByTokenId(refreshTokenJti)) {
            throw new RuntimeException("Invalid Refresh token provided.");
        }

        // generate new access and refresh tokens
        String username = jwtService.extractUserName(refreshToken);
        String role = jwtService.extractGrantedAuthority(refreshToken);
        String[] newTokens = jwtService.generateAccessAndRefreshToken(username, new SimpleGrantedAuthority(role));

        // add old refresh token to blacklist repository
        blacklistTokensRepository.save(new BlacklistTokens(refreshTokenJti, refreshTokenExpiration));

        return newTokens;
    }
}
