package drimer.drimain.controller;

import drimer.drimain.api.dto.*;
import drimer.drimain.api.mapper.ZgloszenieMapper;
import drimer.drimain.model.Dzial;
import drimer.drimain.model.User;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.repository.DzialRepository;
import drimer.drimain.repository.UserRepository;
import drimer.drimain.repository.ZgloszenieRepository;
import drimer.drimain.util.ZgloszenieStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/zgloszenia")
@RequiredArgsConstructor
public class ZgloszenieRestController {

    private final ZgloszenieRepository zgloszenieRepository;
    private final DzialRepository dzialRepository;
    private final UserRepository userRepository;

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
        Zgloszenie z = new Zgloszenie();
        ZgloszenieStatus mapped = ZgloszenieStatusMapper.map(req.getStatus());
        z.setStatus(mapped != null ? mapped : ZgloszenieStatus.OPEN);
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

    @PutMapping("/{id}")
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