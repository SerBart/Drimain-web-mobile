package drimer.drimain.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import drimer.drimain.api.dto.ZgloszenieCreateRequest;
import drimer.drimain.api.dto.ZgloszenieUpdateRequest;
import drimer.drimain.model.User;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.repository.UserRepository;
import drimer.drimain.repository.ZgloszenieRepository;
import drimer.drimain.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * I-FALLBACK Checkpoint: Integration tests for fallback scenarios and error handling
 * 
 * Tests verify that:
 * - Invalid authentication attempts are handled gracefully
 * - Missing or malformed JWT tokens return appropriate errors
 * - Non-existent resource requests return proper HTTP status codes
 * - Invalid data submissions are rejected with meaningful errors
 * - Security exceptions are properly handled and don't expose sensitive information
 */
@SpringBootTest
@AutoConfigureWebMvc
@Transactional
@DisplayName("I-FALLBACK: Security Fallback and Error Handling Integration Tests")
public class I_FallbackIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ZgloszenieRepository zgloszenieRepository;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private MockMvc mockMvc;
    private String validAdminToken;
    private String validUserToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        
        setupTokens();
    }
    
    private void setupTokens() {
        User adminUser = userRepository.findByUsername("admin").orElseThrow();
        User regularUser = userRepository.findByUsername("user").orElseThrow();
        
        validAdminToken = jwtService.generate(adminUser.getUsername(), Map.of());
        validUserToken = jwtService.generate(regularUser.getUsername(), Map.of());
    }

    @Test
    @DisplayName("I-FALLBACK-001: Should handle missing Authorization header")
    void shouldHandleMissingAuthorizationHeader() throws Exception {
        ZgloszenieUpdateRequest updateRequest = new ZgloszenieUpdateRequest();
        updateRequest.setTytul("Unauthorized update attempt");
        
        mockMvc.perform(put("/api/zgloszenia/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("I-FALLBACK-002: Should handle malformed Authorization header")
    void shouldHandleMalformedAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/api/zgloszenia")
                .header("Authorization", "InvalidAuthHeader"))
                .andExpect(status().isUnauthorized());
                
        mockMvc.perform(get("/api/zgloszenia")
                .header("Authorization", "Bearer"))
                .andExpect(status().isUnauthorized());
                
        mockMvc.perform(get("/api/zgloszenia")
                .header("Authorization", "Basic sometoken"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("I-FALLBACK-003: Should handle expired JWT tokens")
    void shouldHandleExpiredJwtTokens() throws Exception {
        // Create a JWT token that's already expired (this is a simulation)
        String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYwOTQ1OTIwMH0.invalid";
        
        mockMvc.perform(get("/api/zgloszenia")
                .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("I-FALLBACK-004: Should handle invalid JWT tokens")
    void shouldHandleInvalidJwtTokens() throws Exception {
        String invalidToken = "invalid.jwt.token.here";
        
        mockMvc.perform(get("/api/zgloszenia")
                .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized());
        
        // Test with completely random string
        mockMvc.perform(get("/api/zgloszenia")
                .header("Authorization", "Bearer randomstring123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("I-FALLBACK-005: Should handle non-existent resource requests")
    void shouldHandleNonExistentResourceRequests() throws Exception {
        Long nonExistentId = 999999L;
        
        // GET non-existent zgloszenie
        mockMvc.perform(get("/api/zgloszenia/" + nonExistentId)
                .header("Authorization", "Bearer " + validAdminToken))
                .andExpect(status().isNotFound());
        
        // UPDATE non-existent zgloszenie
        ZgloszenieUpdateRequest updateRequest = new ZgloszenieUpdateRequest();
        updateRequest.setTytul("Update non-existent");
        
        mockMvc.perform(put("/api/zgloszenia/" + nonExistentId)
                .header("Authorization", "Bearer " + validAdminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
        
        // DELETE non-existent zgloszenie
        mockMvc.perform(delete("/api/zgloszenia/" + nonExistentId)
                .header("Authorization", "Bearer " + validAdminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("I-FALLBACK-006: Should handle invalid request data")
    void shouldHandleInvalidRequestData() throws Exception {
        // Test with empty JSON
        mockMvc.perform(post("/api/zgloszenia")
                .header("Authorization", "Bearer " + validAdminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
        
        // Test with invalid JSON
        mockMvc.perform(post("/api/zgloszenia")
                .header("Authorization", "Bearer " + validAdminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("I-FALLBACK-007: Should handle insufficient permissions gracefully")
    void shouldHandleInsufficientPermissionsGracefully() throws Exception {
        // Create test zgloszenie as admin
        Zgloszenie testZgloszenie = new Zgloszenie();
        testZgloszenie.setTytul("Test for permissions");
        testZgloszenie.setOpis("Test description");
        testZgloszenie.setTyp("AWARIA");
        testZgloszenie.setStatus(ZgloszenieStatus.OPEN);
        testZgloszenie.setImie("Test");
        testZgloszenie.setNazwisko("User");
        testZgloszenie.setAutor(userRepository.findByUsername("admin").orElseThrow());
        testZgloszenie = zgloszenieRepository.save(testZgloszenie);
        
        // Try to update as regular user - should be forbidden
        ZgloszenieUpdateRequest updateRequest = new ZgloszenieUpdateRequest();
        updateRequest.setTytul("Unauthorized update");
        
        mockMvc.perform(put("/api/zgloszenia/" + testZgloszenie.getId())
                .header("Authorization", "Bearer " + validUserToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden());
        
        // Try to delete as regular user - should be forbidden
        mockMvc.perform(delete("/api/zgloszenia/" + testZgloszenie.getId())
                .header("Authorization", "Bearer " + validUserToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("I-FALLBACK-008: Should handle SQL injection attempts")
    void shouldHandleSqlInjectionAttempts() throws Exception {
        // Test SQL injection in path parameter
        String sqlInjectionAttempt = "1'; DROP TABLE zgloszenia; --";
        
        mockMvc.perform(get("/api/zgloszenia/" + sqlInjectionAttempt)
                .header("Authorization", "Bearer " + validAdminToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("I-FALLBACK-009: Should handle XSS attempts in request data")
    void shouldHandleXssAttemptsInRequestData() throws Exception {
        ZgloszenieCreateRequest xssRequest = new ZgloszenieCreateRequest();
        xssRequest.setTytul("<script>alert('XSS')</script>");
        xssRequest.setOpis("<img src='x' onerror='alert(1)'>");
        xssRequest.setTyp("AWARIA");
        xssRequest.setImie("Test<script>");
        xssRequest.setNazwisko("User</script>");
        
        // Should create the record but sanitize the content (depending on implementation)
        // At minimum, it shouldn't cause a server error
        mockMvc.perform(post("/api/zgloszenia")
                .header("Authorization", "Bearer " + validAdminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(xssRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("I-FALLBACK-010: Should handle concurrent access gracefully")
    void shouldHandleConcurrentAccessGracefully() throws Exception {
        // Create test zgloszenie
        Zgloszenie testZgloszenie = new Zgloszenie();
        testZgloszenie.setTytul("Concurrent test");
        testZgloszenie.setOpis("Test description");
        testZgloszenie.setTyp("AWARIA");
        testZgloszenie.setStatus(ZgloszenieStatus.OPEN);
        testZgloszenie.setImie("Test");
        testZgloszenie.setNazwisko("User");
        testZgloszenie.setAutor(userRepository.findByUsername("admin").orElseThrow());
        testZgloszenie = zgloszenieRepository.save(testZgloszenie);
        
        // Simulate concurrent updates (this is a basic test - real concurrent testing would need threading)
        ZgloszenieUpdateRequest updateRequest1 = new ZgloszenieUpdateRequest();
        updateRequest1.setTytul("First update");
        
        ZgloszenieUpdateRequest updateRequest2 = new ZgloszenieUpdateRequest();
        updateRequest2.setTytul("Second update");
        
        // Both should succeed (last one wins in typical scenario)
        mockMvc.perform(put("/api/zgloszenia/" + testZgloszenie.getId())
                .header("Authorization", "Bearer " + validAdminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest1)))
                .andExpect(status().isOk());
        
        mockMvc.perform(put("/api/zgloszenia/" + testZgloszenie.getId())
                .header("Authorization", "Bearer " + validAdminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest2)))
                .andExpect(status().isOk());
    }
}