package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.CustomerRegisterCO;
import com.abhinav.abhinavProject.entity.user.ActivationToken;
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

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomerServiceImpl implements CustomerService {

    UserRepository userRepository;
    CustomerRepository customerRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    EmailServiceImpl emailServiceImpl;

    public Customer registerCustomer(CustomerRegisterCO registerCO) {
        if (userRepository.existsByEmail(registerCO.getEmail())) {
            throw new ValidationException("Email already registered");
        }

        if (!registerCO.getPassword().equals(registerCO.getConfirmPassword())) {
            throw new ValidationException("Password mismatch.");
        }

        Role customerRole = roleRepository.findByAuthority("ROLE_CUSTOMER");

        User user = new User();
        Customer customer = new Customer();

        user.setEmail(registerCO.getEmail());
        user.setPassword(passwordEncoder.encode(registerCO.getPassword()));
        user.setFirstName(registerCO.getFirstName());
        if (registerCO.getMiddleName() != null)
            user.setMiddleName(registerCO.getMiddleName());
        user.setLastName(registerCO.getLastName());
        user.setRole(customerRole);

        customer.setUser(user);
        customer.setContact(Long.parseLong(registerCO.getPhoneNumber()));

        customer = generateNewActivationToken(customer);

        Customer savedCustomer = customerRepository.save(customer);

        emailServiceImpl.sendActivationEmail(customer.getUser().getFirstName(),
                customer.getUser().getEmail(),
                customer.getActivationToken().getToken());

        return savedCustomer;
    }

    private Customer generateNewActivationToken(Customer customer) {
        ActivationToken activationToken = new ActivationToken();

        activationToken.setToken(UUID.randomUUID().toString());
        activationToken.setCustomer(customer);
        activationToken.setExpiration(LocalDateTime.now().plusHours(3));

        customer.setActivationToken(activationToken);
        return customer;
    }

    public void activateCustomerAccount(String token) {
        Customer customer = customerRepository.findByActivationToken_Token(token);

        if (customer == null) {
            throw new RuntimeException("Invalid activation token provided");
        }

        if (customer.getUser().isActive()) {
            throw new RuntimeException("Customer account is already activated");
        }

        if (customer.getActivationToken()
                .getExpiration()
                .isBefore(LocalDateTime.now())
        ) {
            Customer newTokenCustomer = generateNewActivationToken(customer);
            customerRepository.save(newTokenCustomer);
            emailServiceImpl.sendActivationEmail(newTokenCustomer.getUser().getFirstName(),
                    newTokenCustomer.getUser().getEmail(),
                    newTokenCustomer.getActivationToken().getToken());
            throw new RuntimeException("Activation code expired. New code has been emailed.");
        }

        User customerUser = customer.getUser();
        customerUser.setActive(true);
        customer.setUser(customerUser);
        customer.setActivationToken(null);

        customerRepository.save(customer);
    }
}
