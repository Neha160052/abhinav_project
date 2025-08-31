package com.abhinav.abhinavProject;

import com.abhinav.abhinavProject.entity.user.Role;
import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.RoleRepository;
import com.abhinav.abhinavProject.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableAsync
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AbhinavProjectApplication  implements CommandLineRunner {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(AbhinavProjectApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByAuthority("ROLE_ADMIN");
        Optional<User> roleAdmin = userRepository.findByRole(adminRole);

        roleAdmin.ifPresentOrElse(
                user->{},
                ()->{
                    User newAdmin = new User();
                    newAdmin.setFirstName("Admin");
                    newAdmin.setLastName("Istrator");
                    newAdmin.setPassword(passwordEncoder.encode("Admin@1234"));
                    newAdmin.setEmail("admin@mail.com");
                    newAdmin.setRole(adminRole);
                    newAdmin.setActive(true);

                    userRepository.save(newAdmin);
                }
        );
    }
}
