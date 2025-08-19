package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.security.UserPrinciple;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user==null) {
            throw new UsernameNotFoundException("User Not Found");

        }
        return new UserPrinciple(user);
    }
}
