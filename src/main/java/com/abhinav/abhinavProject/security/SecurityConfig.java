package com.abhinav.abhinavProject.security;

import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.service.impl.EmailServiceImpl;
import com.abhinav.abhinavProject.service.impl.UserDetailsServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
                            .requestMatchers(
                                    "/auth/login",
                                    "/auth/forgot-password",
                                    "/api/auth/forgot-password/reset",
                                    "/api/auth/refresh-token"
                            ).permitAll()
                            .requestMatchers(
                                    "/admin/**"
                            ).hasRole("ADMIN")
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
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl,
                                                         PasswordEncoder passwordEncoder,
                                                         EmailServiceImpl emailService,
                                                         UserRepository userRepository) {
        return new CustomDaoAuthenticationProvider(
                userDetailsServiceImpl,
                passwordEncoder,
                emailService,
                userRepository
        );
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
