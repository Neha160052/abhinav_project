package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.SellerRegisterCO;
import com.abhinav.abhinavProject.entity.user.Role;
import com.abhinav.abhinavProject.entity.user.Seller;
import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.RoleRepository;
import com.abhinav.abhinavProject.repository.SellerRepository;
import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.service.SellerService;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SellerServiceImpl implements SellerService {

    UserRepository userRepository;
    SellerRepository sellerRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public Seller registerSeller(SellerRegisterCO registerCO) {
        if(userRepository.existsByEmail(registerCO.getEmail())) {
            throw new ValidationException("Email Already Exists");
        }

        if(!registerCO.getPassword().equals(registerCO.getConfirmPassword())) {
            throw new ValidationException("Password mismatch.");
        }

        Role sellerRole = roleRepository.findByAuthority("ROLE_SELLER");

        User user = new User();
        user.setEmail(registerCO.getEmail());
        user.setPassword(passwordEncoder.encode(registerCO.getPassword()));
        user.setFirstName(registerCO.getFirstName());
        user.setLastName(registerCO.getLastName());
        user.setRole(sellerRole);

        Seller newSeller = new Seller();
        newSeller.setUser(user);
        newSeller.setCompanyName(registerCO.getCompanyName());
        newSeller.setCompanyContact(Long.parseLong(registerCO.getCompanyContact()));
        newSeller.setGst(registerCO.getGst());

        return sellerRepository.save(newSeller);
    }
}
