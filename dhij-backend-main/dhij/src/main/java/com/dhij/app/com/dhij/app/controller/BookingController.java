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
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {
    private final BookingService service;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
 
    public BookingController(BookingService service,
                             JavaMailSender mailSender,
                             UserRepository userRepository) {
        this.service = service;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }
 
    // GET /helpers/{id}/slots?date=YYYY-MM-DD
    @GetMapping("/helpers/{id}/slots")
    public Map<String, Object> slots(@PathVariable Long id, @RequestParam String date) {
        System.out.println("📅 Getting slots for helper " + id + " on date: " + date);
        LocalDate d = LocalDate.parse(date);
        List<String> slots = service.getAvailableSlots(id, d).stream()
                .map(LocalDateTime::toString)
                .collect(Collectors.toList());
        return Map.of("data", slots);
    }
 
    // GET /bookings/my - Get current user's bookings
    @GetMapping("/bookings/my")
    public ResponseEntity<?> getMyBookings(Principal principal) {
        System.out.println("📋 Getting bookings for user: " + principal.getName());
        
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Not logged in"));
        }
        
        List<Booking> bookings = service.getBookingsByUsername(principal.getName());
        
        // Convert to response format
        List<Map<String, Object>> result = bookings.stream()
                .map(b -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", b.getId());
                    item.put("helperId", b.getHelperId());
                    item.put("startTime", b.getStartTime().toString());
                    item.put("endTime", b.getEndTime().toString());
                    item.put("username", b.getUsername());
                    return item;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(Map.of("data", result));
    }
 
    // POST /helpers/{id}/book - Book a session
    @PostMapping("/helpers/{id}/book")
    public ResponseEntity<?> book(@PathVariable Long id,
                                  @RequestBody Map<String, String> body,
                                  Principal principal) {
        
        System.out.println("=================================================");
        System.out.println("🎯 BOOKING REQUEST RECEIVED");
        System.out.println("Helper ID: " + id);
        System.out.println("Username: " + principal.getName());
        System.out.println("=================================================");
        
        String startStr = body.get("start");
        LocalDateTime start = LocalDateTime.parse(startStr);
 
        try {
            // Save booking to database
            Booking b = service.book(id, principal.getName(), start);
            System.out.println("✅ Booking saved to database - Booking ID: " + b.getId());
            
            // === EMAIL SENDING LOGIC ===
            System.out.println("📧 Starting email send process...");
            
            // Find user by username
            Optional<User> userOpt = userRepository.findByUsername(principal.getName());
            
            if (userOpt.isEmpty()) {
                System.err.println("❌ ERROR: User not found in database!");
                System.err.println("   Looking for username: " + principal.getName());
            } else {
                User u = userOpt.get();
                System.out.println("✅ User found in database");
                System.out.println("   Username: " + u.getUsername());
                System.out.println("   Email: " + u.getEmail());
                
                if (u.getEmail() == null || u.getEmail().trim().isEmpty()) {
                    System.err.println("❌ ERROR: User has no email address set!");
                } else {
                    // Create and send email
                    SimpleMailMessage msg = new SimpleMailMessage();
                    msg.setFrom("imrankhan8669393@gmail.com");
                    msg.setTo(u.getEmail());
                    msg.setSubject("Your Session is Booked - DHij App");
                    msg.setText(
                        "Hi " + u.getUsername() + ",\n\n" +
                        "Great news! Your session has been successfully booked.\n\n" +
                        "Booking Details:\n" +
                        "----------------\n" +
                        "Helper ID: " + id + "\n" +
                        "Date: " + b.getStartTime().toLocalDate() + "\n" +
                        "Start Time: " + b.getStartTime().toLocalTime() + "\n" +
                        "End Time: " + b.getEndTime().toLocalTime() + "\n" +
                        "Duration: 45 minutes\n\n" +
                        "Please be on time for your session.\n\n" +
                        "Thanks,\n" +
                        "DHij App Team"
                    );
                    
                    System.out.println("📧 Sending email to: " + u.getEmail());
                    
                    try {
                        mailSender.send(msg);
                        System.out.println("✅✅✅ EMAIL SENT SUCCESSFULLY! ✅✅✅");
                        System.out.println("   Recipient: " + u.getEmail());
                    } catch (Exception emailError) {
                        System.err.println("❌❌❌ EMAIL FAILED! ❌❌❌");
                        System.err.println("Error message: " + emailError.getMessage());
                        System.err.println("Error type: " + emailError.getClass().getName());
                        emailError.printStackTrace();
                    }
                }
            }
            
            System.out.println("=================================================");
            
            // Return success response
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Booked",
                "data", Map.of(
                    "id", b.getId(),
                    "start", b.getStartTime().toString(),
                    "end", b.getEndTime().toString()
                )
            ));
            
        } catch (IllegalStateException busy) {
            System.err.println("❌ Slot already booked!");
            Optional<LocalDateTime> next = service.findNextAvailable(id, start.toLocalDate());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "message", "Slot not available",
                "nextAvailable", next.map(LocalDateTime::toString).orElse(null)
            ));
        } catch (Exception e) {
            System.err.println("❌ UNEXPECTED ERROR during booking!");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Booking failed: " + e.getMessage()));
        }
    }
}
 