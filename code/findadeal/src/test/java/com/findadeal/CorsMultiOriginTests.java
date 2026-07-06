package com.findadeal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "app.cors.allowed-origins=http://localhost:5173,https://demo.example.com")
class CorsMultiOriginTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void secondConfiguredOriginIsAllowed() throws Exception {
        mockMvc.perform(get("/listings").header("Origin", "https://demo.example.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://demo.example.com"));
    }

    @Test
    void unlistedOriginIsStillRejected() throws Exception {
        mockMvc.perform(get("/listings").header("Origin", "https://not-allowed.example.com"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
}
