package drimer.drimain.controller;

import drimer.drimain.api.dto.*;
import drimer.drimain.api.mapper.ZgloszenieMapper;
import drimer.drimain.model.Dzial;
import drimer.drimain.model.User;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.model.enums.ZgloszeniePriorytet;
import drimer.drimain.repository.DzialRepository;
import drimer.drimain.repository.UserRepository;
import drimer.drimain.repository.ZgloszenieRepository;
import drimer.drimain.service.ZgloszenieQueryService;
import drimer.drimain.util.ZgloszenieStatusMapper;
import drimer.drimain.util.ZgloszeniePriorityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/zgloszenia")
@RequiredArgsConstructor
public class ZgloszenieRestController {

    private final ZgloszenieRepository zgloszenieRepository;
    private final DzialRepository dzialRepository;
    private final UserRepository userRepository;
    private final ZgloszenieQueryService zgloszenieQueryService;

    @GetMapping
    @Operation(summary = "List zgloszenia with filtering and pagination")
    public PagedResponse<ZgloszenieDTO> list(
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by typ (LIKE search)") @RequestParam(required = false) String typ,
            @Parameter(description = "Filter by dzial ID") @RequestParam(required = false) Long dzialId,
            @Parameter(description = "Filter by autor ID") @RequestParam(required = false) Long autorId,
            @Parameter(description = "Full-text search in opis, imie, nazwisko, tytul") @RequestParam(required = false) String q,
            @Parameter(description = "Filter by priority") @RequestParam(required = false) String priorytet,
            @Parameter(description = "Filter from date (ISO 8601 format: yyyy-MM-ddTHH:mm:ss)") @RequestParam(required = false) String dataOd,
            @Parameter(description = "Filter to date (ISO 8601 format: yyyy-MM-ddTHH:mm:ss)") @RequestParam(required = false) String dataDo,
            @Parameter(description = "Sort parameter (field,direction), default: createdAt,desc") @RequestParam(defaultValue = "createdAt,desc") String sort,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        // Parse sort parameter
        Sort sortObj = parseSortParameter(sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        // Parse enum parameters
        ZgloszenieStatus statusEnum = ZgloszenieStatusMapper.map(status);
        ZgloszeniePriorytet priortyetEnum = ZgloszeniePriorityMapper.map(priorytet);

        // Parse date parameters
        LocalDateTime dataOdParsed = parseDate(dataOd);
        LocalDateTime dataDoParsed = parseDate(dataDo);

        // Use query service for filtering
        Page<Zgloszenie> results = zgloszenieQueryService.findWithFilters(
                statusEnum, typ, dzialId, autorId, q, priortyetEnum, dataOdParsed, dataDoParsed, pageable);

        // Map to DTOs
        Page<ZgloszenieDTO> dtoPage = results.map(ZgloszenieMapper::toDto);

        return PagedResponse.of(dtoPage, sort);
    }

    private Sort parseSortParameter(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        String[] parts = sort.split(",");
        if (parts.length != 2) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        String field = parts[0].trim();
        String direction = parts[1].trim().toLowerCase();
        
        Sort.Direction sortDirection = "asc".equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        
        return Sort.by(sortDirection, field);
    }

    private LocalDateTime parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected ISO 8601: yyyy-MM-ddTHH:mm:ss");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get zgloszenie by ID")
    public ZgloszenieDTO get(@PathVariable Long id) {
        Zgloszenie z = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found"));
        return ZgloszenieMapper.toDto(z);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new zgloszenie")
    public ZgloszenieDTO create(@Valid @RequestBody ZgloszenieCreateRequest req) {
        Zgloszenie z = new Zgloszenie();
        ZgloszenieStatus mapped = ZgloszenieStatusMapper.map(req.getStatus());
        z.setStatus(mapped != null ? mapped : ZgloszenieStatus.OPEN);
        
        ZgloszeniePriorytet priorityMapped = ZgloszeniePriorityMapper.map(req.getPriorytet());
        z.setPriorytet(priorityMapped != null ? priorityMapped : ZgloszeniePriorytet.MEDIUM);
        
        z.setTyp(req.getTyp());
        z.setImie(req.getImie());
        z.setNazwisko(req.getNazwisko());
        z.setTytul(req.getTytul());
        z.setOpis(req.getOpis());
        z.setDataGodzina(req.getDataGodzina() != null ? req.getDataGodzina() : LocalDateTime.now());
        
        // Handle relations
        if (req.getDzialId() != null) {
            Dzial dzial = dzialRepository.findById(req.getDzialId())
                    .orElseThrow(() -> new IllegalArgumentException("Dzial not found"));
            z.setDzial(dzial);
        }
        if (req.getAutorId() != null) {
            User autor = userRepository.findById(req.getAutorId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            z.setAutor(autor);
        }
        
        // TODO: obsługa zdjęcia
        zgloszenieRepository.save(z);
        return ZgloszenieMapper.toDto(z);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update zgloszenie")  
    public ZgloszenieDTO update(@PathVariable Long id, @RequestBody ZgloszenieUpdateRequest req, 
                                Authentication authentication) {
        // Check if user has edit permissions (ADMIN or BIURO roles)
        if (!hasEditPermissions(authentication)) {
            throw new SecurityException("Access denied. Admin or Biuro role required.");
        }
        
        Zgloszenie z = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found"));
        
        if (req.getTyp() != null) z.setTyp(req.getTyp());
        if (req.getImie() != null) z.setImie(req.getImie());
        if (req.getNazwisko() != null) z.setNazwisko(req.getNazwisko());
        if (req.getTytul() != null) z.setTytul(req.getTytul());
        if (req.getOpis() != null) z.setOpis(req.getOpis());
        if (req.getStatus() != null) {
            ZgloszenieStatus ms = ZgloszenieStatusMapper.map(req.getStatus());
            if (ms != null) z.setStatus(ms);
        }
        if (req.getPriorytet() != null) {
            ZgloszeniePriorytet priorityMapped = ZgloszeniePriorityMapper.map(req.getPriorytet());
            if (priorityMapped != null) z.setPriorytet(priorityMapped);
        }
        
        // Handle relations
        if (req.getDzialId() != null) {
            Dzial dzial = dzialRepository.findById(req.getDzialId())
                    .orElseThrow(() -> new IllegalArgumentException("Dzial not found"));
            z.setDzial(dzial);
        }
        if (req.getAutorId() != null) {
            User autor = userRepository.findById(req.getAutorId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            z.setAutor(autor);
        }
        
        zgloszenieRepository.save(z);
        return ZgloszenieMapper.toDto(z);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete zgloszenie")
    public void delete(@PathVariable Long id, Authentication authentication) {
        // Check if user has delete permissions (ADMIN or BIURO roles)
        if (!hasEditPermissions(authentication)) {
            throw new SecurityException("Access denied. Admin or Biuro role required.");
        }
        zgloszenieRepository.deleteById(id);
    }
    
    /**
     * Check if the authenticated user has edit/delete permissions (ADMIN or BIURO role)
     */
    private boolean hasEditPermissions(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || 
                              a.getAuthority().equals("ROLE_BIURO"));
    }
}