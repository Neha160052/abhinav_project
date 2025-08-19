package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.CustomerRegisterCO;
import com.abhinav.abhinavProject.entity.user.Customer;
import com.abhinav.abhinavProject.entity.user.Role;
import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.CustomerRepository;
import com.abhinav.abhinavProject.repository.RoleRepository;
import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.service.CustomerService;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomerServiceImpl implements CustomerService {

    UserRepository userRepository;
    CustomerRepository customerRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public Customer registerCustomer(CustomerRegisterCO registerCO) {
        if (userRepository.existsByEmail(registerCO.getEmail())) {
            throw new ValidationException("Email already registered");
        }

        if(!registerCO.getPassword().equals(registerCO.getConfirmPassword())) {
            throw new ValidationException("Password mismatch.");
        }

        Role customerRole = roleRepository.findByAuthority("ROLE_CUSTOMER");

        User user = new User();
        user.setEmail(registerCO.getEmail());
        user.setPassword(passwordEncoder.encode(registerCO.getPassword()));
        user.setFirstName(registerCO.getFirstName());
        user.setLastName(registerCO.getLastName());
        user.setRole(customerRole);

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setContact(Long.parseLong(registerCO.getPhoneNumber()));

        return customerRepository.save(customer);
    }
}
