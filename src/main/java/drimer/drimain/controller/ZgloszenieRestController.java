package drimer.drimain.controller;

import drimer.drimain.api.dto.*;
import drimer.drimain.api.mapper.ZgloszenieMapper;
import drimer.drimain.model.Dzial;
import drimer.drimain.model.User;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieEventType;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.repository.DzialRepository;
import drimer.drimain.repository.UserRepository;
import drimer.drimain.repository.ZgloszenieRepository;
import drimer.drimain.service.ZgloszenieCommandService;
import drimer.drimain.service.ZgloszenieSSEService;
import drimer.drimain.util.ZgloszenieStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/zgloszenia")
@RequiredArgsConstructor
public class ZgloszenieRestController {

    private final ZgloszenieRepository zgloszenieRepository;
    private final DzialRepository dzialRepository;
    private final UserRepository userRepository;
    private final ZgloszenieCommandService commandService;
    private final ZgloszenieSSEService sseService;

    @GetMapping
    public List<ZgloszenieDTO> list(@RequestParam Optional<String> status,
                                    @RequestParam Optional<String> typ,
                                    @RequestParam Optional<String> q) {

        return zgloszenieRepository.findAll().stream()
                .filter(z -> status
                        .map(s -> {
                            ZgloszenieStatus ms = ZgloszenieStatusMapper.map(s);
                            return ms != null && ms == z.getStatus();
                        })
                        .orElse(true))
                .filter(z -> typ
                        .map(t -> z.getTyp() != null && z.getTyp().equalsIgnoreCase(t))
                        .orElse(true))
                .filter(z -> q
                        .map(query -> {
                            String qq = query.toLowerCase();
                            return (z.getOpis() != null && z.getOpis().toLowerCase().contains(qq)) ||
                                    (z.getTyp() != null && z.getTyp().toLowerCase().contains(qq)) ||
                                    (z.getImie() != null && z.getImie().toLowerCase().contains(qq)) ||
                                    (z.getNazwisko() != null && z.getNazwisko().toLowerCase().contains(qq)) ||
                                    (z.getTytul() != null && z.getTytul().toLowerCase().contains(qq));
                        })
                        .orElse(true))
                .map(ZgloszenieMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ZgloszenieDTO get(@PathVariable Long id) {
        Zgloszenie z = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found"));
        return ZgloszenieMapper.toDto(z);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZgloszenieDTO create(@RequestBody ZgloszenieCreateRequest req) {
        return commandService.create(req);
    }

    @PutMapping("/{id}")
    public ZgloszenieDTO update(@PathVariable Long id, @RequestBody ZgloszenieUpdateRequest req, 
                                Authentication authentication) {
        // Check if user has edit permissions (ADMIN or BIURO roles)
        if (!hasEditPermissions(authentication)) {
            throw new SecurityException("Access denied. Admin or Biuro role required.");
        }
        
        return commandService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication authentication) {
        // Check if user has delete permissions (ADMIN or BIURO roles)
        if (!hasEditPermissions(authentication)) {
            throw new SecurityException("Access denied. Admin or Biuro role required.");
        }
        commandService.delete(id);
    }

    /**
     * SSE endpoint dla strumienia zdarzeń zgłoszeń.
     * Parametry zapytania:
     * - types: lista typów eventów (CREATED,UPDATED,DELETED...)
     * - dzialId: filtrowanie po ID działu
     * - autorId: filtrowanie po ID autora
     * - full: czy przesyłać pełne dane (true/false, domyślnie false)
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents(@RequestParam(required = false) String types,
                                 @RequestParam(required = false) Long dzialId,
                                 @RequestParam(required = false) Long autorId,
                                 @RequestParam(defaultValue = "false") boolean full,
                                 Authentication authentication) {
        // Sprawdź autoryzację
        if (authentication == null) {
            throw new SecurityException("Authentication required for SSE stream");
        }

        // Parsuj typy eventów
        Set<ZgloszenieEventType> eventTypes = parseEventTypes(types);

        // Stwórz subskrypcję
        SseEmitter emitter = sseService.subscribe(eventTypes, dzialId, autorId, full);
        
        if (emitter == null) {
            // Limit subskrypcji przekroczony
            throw new IllegalStateException("Maximum number of SSE connections reached");
        }

        return emitter;
    }

    private Set<ZgloszenieEventType> parseEventTypes(String types) {
        if (types == null || types.isEmpty()) {
            return Set.of(); // pusty set = wszystkie typy
        }
        
        Set<ZgloszenieEventType> result = new HashSet<>();
        for (String type : types.split(",")) {
            try {
                result.add(ZgloszenieEventType.valueOf(type.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Zignoruj niepoprawne typy
            }
        }
        return result;
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