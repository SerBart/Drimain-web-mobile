package drimer.drimain.controller;

import drimer.drimain.api.dto.*;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.service.ZgloszenieService;
import drimer.drimain.util.ZgloszenieStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Zgłoszenia with department-based access control
 */
@RestController
@RequestMapping("/api/zgloszenia")
@RequiredArgsConstructor
public class ZgloszenieRestController {

    private final ZgloszenieService zgloszenieService;

    @GetMapping
    public PageDTO<ZgloszenieDTO> list(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            Authentication authentication) {
        
        // Parse sort parameter
        String[] sortParts = sort.split(",");
        String sortProperty = sortParts.length > 0 ? sortParts[0] : "createdAt";
        Sort.Direction sortDirection = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1]) 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty));
        ZgloszenieStatus statusEnum = status != null ? ZgloszenieStatusMapper.map(status) : null;
        
        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        
        return zgloszenieService.findAll(username, roles, statusEnum, pageable);
    }

    @GetMapping("/{id}")
    public ZgloszenieDTO get(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        
        return zgloszenieService.findById(id, username, roles);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZgloszenieDTO create(@RequestBody ZgloszenieCreateRequest request, Authentication authentication) {
        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        
        return zgloszenieService.create(request, username, roles);
    }

    @PutMapping("/{id}")
    public ZgloszenieDTO update(@PathVariable Long id, @RequestBody ZgloszenieUpdateRequest request, Authentication authentication) {
        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        
        return zgloszenieService.update(id, request, username, roles);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        
        zgloszenieService.delete(id, username, roles);
    }
}