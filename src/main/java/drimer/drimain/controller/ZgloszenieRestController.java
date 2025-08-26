package drimer.drimain.controller;

import drimer.drimain.api.dto.*;
import drimer.drimain.model.User;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.service.CustomUserDetailsService;
import drimer.drimain.service.ZgloszenieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/zgloszenia")
@RequiredArgsConstructor
public class ZgloszenieRestController {

    private final ZgloszenieService zgloszenieService;
    private final CustomUserDetailsService userDetailsService;

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(required = false) String status,
                                 Pageable pageable,
                                 Authentication authentication) {
        try {
            User currentUser = userDetailsService.getUserByUsername(authentication.getName());
            boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
            boolean isBiuro = hasRole(authentication, "ROLE_BIURO");

            ZgloszenieStatus statusEnum = null;
            if (status != null && !status.isEmpty()) {
                try {
                    statusEnum = ZgloszenieStatus.valueOf(status.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(createErrorResponse("Invalid status: " + status));
                }
            }

            Page<Zgloszenie> zgloszenia = zgloszenieService.listForUser(currentUser, statusEnum, pageable, isAdmin, isBiuro);
            
            return ResponseEntity.ok(zgloszenia.map(this::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving zgłoszenia: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id, Authentication authentication) {
        try {
            User currentUser = userDetailsService.getUserByUsername(authentication.getName());
            boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
            boolean isBiuro = hasRole(authentication, "ROLE_BIURO");

            Zgloszenie zgloszenie = zgloszenieService.get(id, currentUser, isAdmin, isBiuro);
            return ResponseEntity.ok(toDto(zgloszenie));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(createErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving zgłoszenie: " + e.getMessage()));
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody ZgloszenieCreateRequest req, Authentication authentication) {
        try {
            User currentUser = userDetailsService.getUserByUsername(authentication.getName());
            boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
            boolean isBiuro = hasRole(authentication, "ROLE_BIURO");

            Zgloszenie zgloszenie = zgloszenieService.create(req, currentUser, isAdmin, isBiuro);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(zgloszenie));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error creating zgłoszenie: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ZgloszenieUpdateRequest req, 
                                   Authentication authentication) {
        try {
            User currentUser = userDetailsService.getUserByUsername(authentication.getName());
            boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
            boolean isBiuro = hasRole(authentication, "ROLE_BIURO");

            Zgloszenie zgloszenie = zgloszenieService.update(id, req, currentUser, isAdmin, isBiuro);
            return ResponseEntity.ok(toDto(zgloszenie));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(createErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error updating zgłoszenie: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        try {
            User currentUser = userDetailsService.getUserByUsername(authentication.getName());
            boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
            boolean isBiuro = hasRole(authentication, "ROLE_BIURO");

            zgloszenieService.delete(id, currentUser, isAdmin, isBiuro);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(createErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error deleting zgłoszenie: " + e.getMessage()));
        }
    }

    private ZgloszenieDTO toDto(Zgloszenie z) {
        ZgloszenieDTO dto = new ZgloszenieDTO();
        dto.setId(z.getId());
        dto.setTytul(z.getTytul());
        dto.setOpis(z.getOpis());
        dto.setStatus(z.getStatus());
        dto.setCreatedAt(z.getCreatedAt());
        dto.setUpdatedAt(z.getUpdatedAt());
        
        if (z.getDzial() != null) {
            dto.setDzialId(z.getDzial().getId());
            dto.setDzialNazwa(z.getDzial().getNazwa());
        }
        
        if (z.getAutor() != null) {
            dto.setAutorUsername(z.getAutor().getUsername());
        }
        
        return dto;
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }

    private Map<String, String> createErrorResponse(String message) {
        return Map.of("error", message);
    }
}