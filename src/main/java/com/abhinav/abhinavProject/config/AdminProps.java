package com.abhinav.abhinavProject.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
@Getter
@Setter
public class AdminProps {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
}

