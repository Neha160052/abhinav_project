package com.abhinav.abhinavProject;

import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.RoleRepository;
import com.abhinav.abhinavProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableAsync
public class AbhinavProjectApplication  implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AbhinavProjectApplication(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(AbhinavProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<User> roleAdmin = userRepository.findByRole("ROLE_ADMIN");

        roleAdmin.ifPresentOrElse(
                user->{},
                ()->{
                    User newAdmin = new User();
                    newAdmin.setFirstName("Admin");
                    newAdmin.setLastName("Istrator");
                    newAdmin.setPassword("admin@123");
                    newAdmin.setEmail("admin@mail.com");
                    newAdmin.setRole(roleRepository.findByAuthority("ROLE_ADMIN"));
                }

        );
        LocalDateTime
    }
}
