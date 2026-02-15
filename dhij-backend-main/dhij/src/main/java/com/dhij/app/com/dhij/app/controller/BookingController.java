package com.dhij.app.com.dhij.app.controller;

import com.dhij.app.com.dhij.app.model.Booking;
import com.dhij.app.com.dhij.app.model.User;
import com.dhij.app.com.dhij.app.repository.UserRepository;
import com.dhij.app.com.dhij.app.service.BookingService;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/helpers")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {
    private final BookingService service;
    private final JavaMailSender mailSender;        // ⬅️ NEW
    private final UserRepository userRepository;    // ⬅️ NEW

    public BookingController(BookingService service,
                             JavaMailSender mailSender,
                             UserRepository userRepository) {
        this.service = service;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    // GET /helpers/{id}/slots?date=YYYY-MM-DD
    @GetMapping("/{id}/slots")
    public Map<String, Object> slots(@PathVariable Long id, @RequestParam String date) {
        LocalDate d = LocalDate.parse(date);
        List<String> slots = service.getAvailableSlots(id, d).stream()
                .map(LocalDateTime::toString) // ISO-8601
                .collect(Collectors.toList());
        return Map.of("data", slots);
    }

    // POST /helpers/{id}/book  { "start": "2026-01-28T10:00:00" }
    @PostMapping("/{id}/book")
    public ResponseEntity<?> book(@PathVariable Long id,
                                  @RequestBody Map<String,String> body,
                                  Principal principal) {
        String startStr = body.get("start");
        LocalDateTime start = LocalDateTime.parse(startStr);

        try {
            Booking b = service.book(id, principal.getName(), start);

            // --- Send confirmation email to the logged-in user ---
            Optional<User> userOpt = userRepository.findByUsername(principal.getName());
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(u.getEmail());
                msg.setSubject("Your Session is Booked");
                msg.setText(
                    "Hi " + u.getUsername() + ",\n\n" +
                    "Your session has been booked.\n" +
                    "Helper ID: " + id + "\n" +
                    "Start: " + b.getStartTime() + "\n" +
                    "End: " + b.getEndTime() + "\n\n" +
                    "Thanks,\nDHij App"
                );
                mailSender.send(msg);
            }
            // ------------------------------------------------------

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Booked",
                "data", Map.of(
                    "id", b.getId(),
                    "start", b.getStartTime().toString(),
                    "end", b.getEndTime().toString()
                )
            ));
        } catch (IllegalStateException busy) {
            Optional<LocalDateTime> next = service.findNextAvailable(id, start.toLocalDate());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "message", "Slot not available",
                "nextAvailable", next.map(LocalDateTime::toString).orElse(null)
            ));
        }
    }
}