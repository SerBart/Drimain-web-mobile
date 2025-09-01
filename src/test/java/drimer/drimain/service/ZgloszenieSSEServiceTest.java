package drimer.drimain.service;

import drimer.drimain.model.enums.ZgloszenieEventType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ZgloszenieSSEServiceTest {

    @Test
    void shouldCreateSubscriptionAndHandleFilters() {
        // Given
        ZgloszenieSSEService sseService = new ZgloszenieSSEService(10, 1, 5);
        Set<ZgloszenieEventType> eventTypes = Set.of(ZgloszenieEventType.CREATED, ZgloszenieEventType.UPDATED);

        // When
        SseEmitter emitter = sseService.subscribe(eventTypes, 1L, 2L, true);

        // Then
        assertNotNull(emitter, "SSE emitter should be created");
        assertEquals(1, sseService.getActiveSubscriptions(), "Should have 1 active subscription");
        
        // Test empty event types
        SseEmitter emitter2 = sseService.subscribe(Set.of(), null, null, false);
        assertNotNull(emitter2, "Should accept empty event types (all events)");
        assertEquals(2, sseService.getActiveSubscriptions(), "Should have 2 active subscriptions");
    }

    @Test 
    void shouldHandleMaxClientsLimit() {
        // Given
        ZgloszenieSSEService sseService = new ZgloszenieSSEService(2, 1, 5); // max 2 clients

        // When - add 2 clients (max)
        SseEmitter emitter1 = sseService.subscribe(Set.of(), null, null, false);
        SseEmitter emitter2 = sseService.subscribe(Set.of(), null, null, false);

        // Then
        assertNotNull(emitter1, "First client should be accepted");
        assertNotNull(emitter2, "Second client should be accepted");
        assertEquals(2, sseService.getActiveSubscriptions(), "Should have 2 active subscriptions");

        // When - try to add 3rd client
        SseEmitter emitter3 = sseService.subscribe(Set.of(), null, null, false);

        // Then
        assertNull(emitter3, "Should reject subscription when max clients reached");
        assertEquals(2, sseService.getActiveSubscriptions(), "Should still have 2 active subscriptions");
    }
}