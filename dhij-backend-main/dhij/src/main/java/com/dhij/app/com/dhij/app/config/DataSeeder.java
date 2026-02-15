package com.dhij.app.com.dhij.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dhij.app.com.dhij.app.model.User;
import com.dhij.app.com.dhij.app.repository.UserRepository;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedUsers(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                User u = new User();
                u.setUsername("admin");
                u.setPassword(encoder.encode("admin123")); // store HASHED password
                u.setRole("ADMIN");
                u.setEmail("imrankhan8669393@gmail.com");
                userRepo.save(u);
            }
        };
    }
}
