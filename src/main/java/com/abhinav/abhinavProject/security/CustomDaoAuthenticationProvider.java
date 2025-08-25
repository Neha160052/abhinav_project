package com.abhinav.abhinavProject.security;

import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.service.impl.EmailServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    UserRepository userRepository;
    EmailServiceImpl emailService;

    public CustomDaoAuthenticationProvider(UserDetailsService userDetailsService,
                                           PasswordEncoder passwordEncoder,
                                           EmailServiceImpl emailService,
                                           UserRepository userRepository) {
        super(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        try {
            super.additionalAuthenticationChecks(userDetails, authentication);
            userRepository.findByEmail(userDetails.getUsername())
                    .ifPresent(user -> {
                        if (user.getInvalidAttemptCount() > 0) {
                            user.setInvalidAttemptCount(0);
                            userRepository.save(user);
                        }
                    });
        } catch (BadCredentialsException ex) {
            userRepository.findByEmail(userDetails.getUsername())
                    .ifPresent(user -> {
                        user.setInvalidAttemptCount(user.getInvalidAttemptCount() + 1);
                        if (user.getInvalidAttemptCount() >= 3) {
                            user.setLocked(true);
                            userRepository.save(user);
//                            emailService.accountLockedNotification(new AccountLockedDto(userDetails.getUsername(), u.getFirstName()));
                            throw new LockedException("User has been locked, exceeded invalid password count.");
                        }
                        userRepository.save(user);
                        throw ex;
                    });
        }
    }

}
