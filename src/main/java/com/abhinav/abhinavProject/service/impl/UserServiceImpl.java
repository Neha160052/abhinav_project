package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    private final EmailServiceImpl emailServiceImpl;

    @Override
    public String activateUserAccount(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Invalid User id provided")
        );

        if(user.isActive()) {
            return "User is already activated.";
        }

        user.setActive(true);
        userRepository.save(user);
        emailServiceImpl.sendAdminActivationMail(user);

        return "User account has been activated successfully.";
    }

    @Override
    public String deactivateUserAccount(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Invalid User id provided")
        );

        if(!user.isActive()) {
            return "User is already deactivated.";
        }

        user.setActive(false);
        userRepository.save(user);
        emailServiceImpl.sendAdminDeactivationMail(user);

        return "User account has been deactivated successfully.";
    }
}
