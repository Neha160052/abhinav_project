package com.abhinav.abhinavProject.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SecurityConfig {

    JwtSecurityFilter jwtSecurityFilter;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(requests ->
                    requests.requestMatchers("/customer/register", "/customer/activate").permitAll()
                            .requestMatchers("/seller/register").permitAll()
                            .requestMatchers("/auth/**").permitAll()
                            .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
