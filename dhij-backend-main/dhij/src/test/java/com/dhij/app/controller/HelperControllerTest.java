package com.dhij.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.dhij.app.com.dhij.app.DhijApplication.class)

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(com.dhij.app.config.TestSecurityConfig.class)
public class HelperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetAllHelpers() throws Exception {
        mockMvc.perform(get("/helpers"))
               .andExpect(status().isOk());
    }

    @Test
    void shouldCreateHelper() throws Exception {

        String helperJson =
                "{"
              + "\"name\":\"John\","
              + "\"role\":\"Counsellor\","
              + "\"specialty\":\"Mental Health\""
              + "}";

        mockMvc.perform(
                post("/helpers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(helperJson)
        ).andExpect(status().isCreated());
    }
}
