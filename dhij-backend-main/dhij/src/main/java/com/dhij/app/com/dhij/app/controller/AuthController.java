package com.dhij.app.com.dhij.app.controller;
 
import com.dhij.app.com.dhij.app.dto.RegisterRequest;
import com.dhij.app.com.dhij.app.model.User;
import com.dhij.app.com.dhij.app.repository.UserRepository;
import com.dhij.app.com.dhij.app.config.JwtUtil;
import com.dhij.app.com.dhij.app.dto.AuthRequest;
import com.dhij.app.com.dhij.app.dto.AuthResponse;
import jakarta.validation.Valid;
 
import java.util.Map;
 
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender mailSender;  // ✅ ADD THIS
 
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          JavaMailSender mailSender) {  // ✅ ADD THIS
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.mailSender = mailSender;  // ✅ ADD THIS
    }
 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));
 
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
 
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
 
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        // Duplicate checks
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
        user.setRole(normalizeRole(request.getRole()));
        user.setEmail(request.getEmail());
 
        user = userRepository.save(user);
 
        // ✅ SEND WELCOME EMAIL
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setSubject("Welcome to DHij App!");
            msg.setText(
                "Hi " + user.getUsername() + ",\n\n" +
                "Welcome to DHij App! 🎉\n\n" +
                "Your account has been created successfully.\n\n" +
                "You can now login and book sessions with our professional helpers.\n\n" +
                "Get started: http://localhost:5173/login\n\n" +
                "Thanks,\nDHij Team"
            );
            mailSender.send(msg);
            System.out.println("✅ Welcome email sent to: " + user.getEmail());
        } catch (Exception e) {
            // Email failed but registration still succeeded
            System.err.println("❌ Failed to send welcome email: " + e.getMessage());
            e.printStackTrace();
        }
 
        // Generate JWT and return
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
 