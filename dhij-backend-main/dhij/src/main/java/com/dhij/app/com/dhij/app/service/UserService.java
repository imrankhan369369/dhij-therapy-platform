package com.dhij.app.com.dhij.app.service;

import com.dhij.app.com.dhij.app.dto.RegisterRequest;
import com.dhij.app.com.dhij.app.events.UserRegisteredEvent;
import com.dhij.app.com.dhij.app.model.User;
import com.dhij.app.com.dhij.app.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.publisher = publisher;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(normalizeRole(request.getRole()));
        user.setEmail(request.getEmail());

        User saved = userRepository.save(user);

        // Publish event AFTER save; listener sends email asynchronously
        publisher.publishEvent(new UserRegisteredEvent(saved.getId(), saved.getEmail(), saved.getUsername()));

        return saved;
    }

    private String normalizeRole(String raw) {
        if (raw == null || raw.isBlank()) return "ROLE_USER";
        String r = raw.toUpperCase();
        return r.startsWith("ROLE_") ? r : "ROLE_" + r;
    }
}