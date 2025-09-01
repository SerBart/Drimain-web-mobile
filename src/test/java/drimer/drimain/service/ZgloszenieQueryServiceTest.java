package drimer.drimain.service;

import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.model.enums.ZgloszeniePriorytet;
import drimer.drimain.repository.ZgloszenieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZgloszenieQueryServiceTest {

    @Mock
    private ZgloszenieRepository zgloszenieRepository;

    @InjectMocks
    private ZgloszenieQueryService zgloszenieQueryService;

    private Zgloszenie testZgloszenie;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testZgloszenie = new Zgloszenie();
        testZgloszenie.setId(1L);
        testZgloszenie.setTyp("Test Type");
        testZgloszenie.setImie("John");
        testZgloszenie.setNazwisko("Doe");
        testZgloszenie.setOpis("Test description for testing");
        testZgloszenie.setStatus(ZgloszenieStatus.OPEN);
        testZgloszenie.setPriorytet(ZgloszeniePriorytet.HIGH);
        testZgloszenie.setDataGodzina(LocalDateTime.now());

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testFindWithFilters_noFilters() {
        Page<Zgloszenie> expectedPage = new PageImpl<>(List.of(testZgloszenie));
        when(zgloszenieRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Zgloszenie> result = zgloszenieQueryService.findWithFilters(
                null, null, null, null, null, null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testZgloszenie, result.getContent().get(0));
    }

    @Test
    void testFindWithFilters_withStatus() {
        Page<Zgloszenie> expectedPage = new PageImpl<>(List.of(testZgloszenie));
        when(zgloszenieRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Zgloszenie> result = zgloszenieQueryService.findWithFilters(
                ZgloszenieStatus.OPEN, null, null, null, null, null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindWithFilters_withPriority() {
        Page<Zgloszenie> expectedPage = new PageImpl<>(List.of(testZgloszenie));
        when(zgloszenieRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Zgloszenie> result = zgloszenieQueryService.findWithFilters(
                null, null, null, null, null, ZgloszeniePriorytet.HIGH, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindWithFilters_withTextSearch() {
        Page<Zgloszenie> expectedPage = new PageImpl<>(List.of(testZgloszenie));
        when(zgloszenieRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Zgloszenie> result = zgloszenieQueryService.findWithFilters(
                null, null, null, null, "test", null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindWithFilters_withDateRange() {
        Page<Zgloszenie> expectedPage = new PageImpl<>(List.of(testZgloszenie));
        when(zgloszenieRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        LocalDateTime dataOd = LocalDateTime.now().minusDays(1);
        LocalDateTime dataDo = LocalDateTime.now().plusDays(1);

        Page<Zgloszenie> result = zgloszenieQueryService.findWithFilters(
                null, null, null, null, null, null, dataOd, dataDo, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindWithFilters_combinedFilters() {
        Page<Zgloszenie> expectedPage = new PageImpl<>(List.of(testZgloszenie));
        when(zgloszenieRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Zgloszenie> result = zgloszenieQueryService.findWithFilters(
                ZgloszenieStatus.OPEN, "test", 1L, 1L, "john", ZgloszeniePriorytet.HIGH, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}