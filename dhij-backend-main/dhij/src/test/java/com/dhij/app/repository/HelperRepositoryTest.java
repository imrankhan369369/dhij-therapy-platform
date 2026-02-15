
package com.dhij.app.repository;

import com.dhij.app.com.dhij.app.DhijApplication;
import com.dhij.app.com.dhij.app.model.Helper;
import com.dhij.app.com.dhij.app.repository.HelperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DhijApplication.class)
@ActiveProfiles("test")
@Transactional
@Import(HelperRepositoryTest.TestBeans.class)  // <-- ensure the bean is imported
class HelperRepositoryTest {

    @TestConfiguration
    static class TestBeans {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private HelperRepository helperRepository;

    @BeforeEach
    void setUp() {
        helperRepository.deleteAll();
    }

    @Test
    void saveHelper_shouldPersistData() {
        Helper helper = new Helper();
        helper.setName("Rahul");
        helper.setRole("Volunteer");
        helper.setSpecialty("Medical");

        Helper saved = helperRepository.save(helper);

        assertNotNull(saved.getId());
        assertEquals("Rahul", saved.getName());
        assertEquals("Volunteer", saved.getRole());
        assertEquals("Medical", saved.getSpecialty());
    }

    @Test
    void findAll_shouldReturnHelpers() {
        Helper h1 = new Helper();
        h1.setName("Asha");
        h1.setRole("Advisor");
        h1.setSpecialty("Legal");

        Helper h2 = new Helper();
        h2.setName("Kiran");
        h2.setRole("Tutor");
        h2.setSpecialty("Education");

        helperRepository.save(h1);
        helperRepository.save(h2);

        List<Helper> helpers = helperRepository.findAll();

        assertEquals(2, helpers.size());
        assertTrue(helpers.stream().anyMatch(h -> "Asha".equals(h.getName())));
        assertTrue(helpers.stream().anyMatch(h -> "Kiran".equals(h.getName())));
    }
}
