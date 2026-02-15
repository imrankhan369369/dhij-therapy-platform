// src/main/java/com/dhij/app/com/dhij/app/controller/AuthController.java
package com.dhij.app.com.dhij.app.controller;

import com.dhij.app.com.dhij.app.dto.RegisterRequest;
import com.dhij.app.com.dhij.app.model.User;
import com.dhij.app.com.dhij.app.repository.UserRepository;
import com.dhij.app.com.dhij.app.config.JwtUtil; // adjust package if different
import com.dhij.app.com.dhij.app.dto.AuthRequest;
import com.dhij.app.com.dhij.app.dto.AuthResponse; // adjust if you have a different response DTO
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        // ✅ correct BCrypt verification
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        // Duplicate checks (return a message map instead of AuthResponse with null)
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Username already exists"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already exists"));
        }

        // Create and save user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(normalizeRole(request.getRole()));    // e.g. ROLE_USER
        user.setEmail(request.getEmail());

        user = userRepository.save(user);

        // Generate JWT and return AuthResponse(token)
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token));
    }

    private String normalizeRole(String raw) {
        if (raw == null || raw.isBlank()) return "ROLE_USER";
        String r = raw.toUpperCase();
        return r.startsWith("ROLE_") ? r : "ROLE_" + r;
    }
}
