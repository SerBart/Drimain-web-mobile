package drimer.drimain.api.controller;

import drimer.drimain.api.dto.ZgloszenieNewDTO;
import drimer.drimain.api.dto.ZgloszenieNewCreateRequest;
import drimer.drimain.api.dto.ZgloszenieNewUpdateRequest;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.service.ZgloszenieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST API controller for Zgloszenia management.
 * Separate from legacy MVC controllers.
 */
@RestController
@RequestMapping("/api/v2/zgloszenia")
@RequiredArgsConstructor
public class ZgloszenieApiController {

    private final ZgloszenieService zgloszenieService;

    /**
     * GET /api/v2/zgloszenia - List zgloszenia with pagination and optional status filter
     */
    @GetMapping
    public ResponseEntity<Page<ZgloszenieNewDTO>> listZgloszenia(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) ZgloszenieStatus status,
            Authentication authentication) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ZgloszenieNewDTO> result = zgloszenieService.list(pageable, status, authentication);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/v2/zgloszenia - Create new zgloszenie
     */
    @PostMapping
    public ResponseEntity<ZgloszenieNewDTO> createZgloszenie(
            @RequestBody ZgloszenieNewCreateRequest request,
            Authentication authentication) {
        
        ZgloszenieNewDTO created = zgloszenieService.create(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET /api/v2/zgloszenia/{id} - Get zgloszenie by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ZgloszenieNewDTO> getZgloszenie(
            @PathVariable Long id,
            Authentication authentication) {
        
        ZgloszenieNewDTO zgloszenie = zgloszenieService.get(id, authentication);
        return ResponseEntity.ok(zgloszenie);
    }

    /**
     * PUT /api/v2/zgloszenia/{id} - Update zgloszenie
     */
    @PutMapping("/{id}")
    public ResponseEntity<ZgloszenieNewDTO> updateZgloszenie(
            @PathVariable Long id,
            @RequestBody ZgloszenieNewUpdateRequest request,
            Authentication authentication) {
        
        ZgloszenieNewDTO updated = zgloszenieService.update(id, request, authentication);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/v2/zgloszenia/{id} - Delete zgloszenie
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZgloszenie(
            @PathVariable Long id,
            Authentication authentication) {
        
        zgloszenieService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }
}