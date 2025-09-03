package com.abhinav.abhinavProject.config;

import com.abhinav.abhinavProject.entity.user.Role;
import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.exception.RoleNotFoundException;
import com.abhinav.abhinavProject.repository.RoleRepository;
import com.abhinav.abhinavProject.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class BootstrapRunner implements CommandLineRunner {

    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AdminProps adminProps;
    List<String> roles = List.of("ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_SELLER");

    @Override
    public void run(String... args) {

        for (String role : roles) {
            if (roleRepository.findByAuthority(role).isEmpty()) {
                Role newRole = new Role();
                newRole.setAuthority(role);
                roleRepository.save(newRole);
                log.info("Role created: {}", role);
            }
        }

        Optional<User> adminOptional = userRepository.findByEmail(adminProps.getEmail());

        if (adminOptional.isEmpty()) {
            Role adminRole = roleRepository.findByAuthority("ROLE_ADMIN")
                    .orElseThrow(() -> new RoleNotFoundException("Role not found"));

            User admin = new User();
            admin.setFirstName(adminProps.getFirstname());
            admin.setLastName(adminProps.getLastname());
            admin.setPassword(passwordEncoder.encode(adminProps.getPassword()));
            admin.setEmail(adminProps.getEmail());
            admin.setRole(adminRole);
            admin.setActive(true);

            userRepository.save(admin);
            log.info("Admin user created successfully: {}", admin.getEmail());
        } else {
            log.info("Admin user already exists: {}", adminProps.getEmail());
        }
    }
}
