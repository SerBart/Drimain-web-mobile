package drimer.drimain.controller;

import drimer.drimain.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthControllerTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void jwtServiceShouldHaveGetTtlMinutesMethod() {
        // Verify the getTtlMinutes method exists and returns a positive value
        long ttlMinutes = jwtService.getTtlMinutes();
        assertTrue(ttlMinutes > 0, "TTL minutes should be positive");
    }

    // TODO: Add full integration test for /api/auth/login when test user setup is available
    // For now, we verify that the core components work together correctly
}