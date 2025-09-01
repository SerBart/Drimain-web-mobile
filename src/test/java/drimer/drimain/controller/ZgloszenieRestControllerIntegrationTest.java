package drimer.drimain.controller;

import drimer.drimain.api.dto.ZgloszenieCreateRequest;
import drimer.drimain.service.ZgloszenieQueryService;
import drimer.drimain.repository.ZgloszenieRepository;
import drimer.drimain.repository.DzialRepository;
import drimer.drimain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ZgloszenieRestControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    void testGetZgloszenia_withPagination() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                createURLWithPort("/api/zgloszenia?page=0&size=10"), String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\":"));
        assertTrue(response.getBody().contains("\"page\":0"));
    }

    @Test
    void testGetZgloszenia_withFiltering() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                createURLWithPort("/api/zgloszenia?status=OPEN&priorytet=HIGH&q=test"), String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\":"));
    }

    @Test
    void testCreateZgloszenie_validData() {
        ZgloszenieCreateRequest request = new ZgloszenieCreateRequest();
        request.setTyp("Test Type");
        request.setImie("John");
        request.setNazwisko("Doe");
        request.setOpis("This is a test description with more than 10 characters");
        request.setPriorytet("HIGH");
        request.setStatus("OPEN");
        request.setDataGodzina(LocalDateTime.now());

        ResponseEntity<String> response = restTemplate.postForEntity(
                createURLWithPort("/api/zgloszenia"), request, String.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"typ\":\"Test Type\""));
        assertTrue(response.getBody().contains("\"priorytet\":\"HIGH\""));
    }

    @Test
    void testCreateZgloszenie_invalidData_shouldReturn422() {
        ZgloszenieCreateRequest request = new ZgloszenieCreateRequest();
        // Missing required fields - should trigger validation

        ResponseEntity<String> response = restTemplate.postForEntity(
                createURLWithPort("/api/zgloszenia"), request, String.class);
        
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"code\":\"VALIDATION_ERROR\""));
    }

    @Test
    void testGetZgloszenieById_nonExistent_shouldReturn400() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                createURLWithPort("/api/zgloszenia/999"), String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"code\":\"INVALID_ARGUMENT\""));
    }
}