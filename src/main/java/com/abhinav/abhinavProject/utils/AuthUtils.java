package com.abhinav.abhinavProject.utils;

import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthUtils {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public void resetUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
