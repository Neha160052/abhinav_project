package com.abhinav.abhinavProject;

import com.abhinav.abhinavProject.config.AdminProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableAsync
@EnableConfigurationProperties(AdminProps.class)
public class AbhinavProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbhinavProjectApplication.class, args);
    }
}
