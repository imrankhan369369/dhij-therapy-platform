package com.dhij.app.com.dhij.app.config;
 
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
 
import com.dhij.app.com.dhij.app.model.Helper;
import com.dhij.app.com.dhij.app.model.User;
import com.dhij.app.com.dhij.app.repository.HelperRepository;
import com.dhij.app.com.dhij.app.repository.UserRepository;
 
@Configuration
public class DataSeeder {
 
    @Bean
    CommandLineRunner seedData(
            UserRepository userRepo,
            HelperRepository helperRepo,
            PasswordEncoder encoder) {
 
        return args -> {
 
            // ===========================
            // SEED ADMIN USER
            // ===========================
            if (userRepo.findByUsername("admin").isEmpty()) {
                User u = new User();
                u.setUsername("admin");
                u.setPassword(encoder.encode("admin123"));
                u.setRole("ADMIN");
                u.setEmail("imrankhan8669393@gmail.com");
                userRepo.save(u);
                System.out.println("✅ Admin user created!");
            }
 
            // ===========================
            // SEED 4 HELPERS
            // ===========================
            if (helperRepo.count() == 0) {
 
                Helper h1 = new Helper();
                h1.setName("Deepika");
                h1.setRole("Emotional Support");
                h1.setSpecialty("Calm, Empathetic, Active listener");
                helperRepo.save(h1);
 
                Helper h2 = new Helper();
                h2.setName("Harshada");
                h2.setRole("Spiritual Guidance");
                h2.setSpecialty("Mindfulness, Clarity");
                helperRepo.save(h2);
 
                Helper h3 = new Helper();
                h3.setName("Imran");
                h3.setRole("Financial Guidance");
                h3.setSpecialty("Budgeting, Career planning");
                helperRepo.save(h3);
 
                Helper h4 = new Helper();
                h4.setName("Jeevita");
                h4.setRole("General Support");
                h4.setSpecialty("Friendly, Non-judgmental");
                helperRepo.save(h4);
 
                System.out.println("✅ 4 Helpers seeded successfully!");
            }
        };
    }
}
 