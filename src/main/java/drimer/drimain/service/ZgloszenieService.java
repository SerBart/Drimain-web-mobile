package drimer.drimain.service;

import drimer.drimain.api.dto.ZgloszenieCreateRequest;
import drimer.drimain.api.dto.ZgloszenieDTO;
import drimer.drimain.api.dto.ZgloszeniePage;
import drimer.drimain.api.dto.ZgloszenieUpdateRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZgloszenieService {

    private final ZgloszenieRepository repo;
    private final UserRepository userRepository;
    private final DzialRepository dzialRepository;

    /**
     * List zgłoszenia with pagination and security scoping
     */
    public ZgloszeniePage list(int page, int size, String status, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        ZgloszenieStatus statusEnum = ZgloszenieStatusMapper.map(status);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Zgloszenie> result;
        
        if (hasFullAccess(currentUser)) {
            // ROLE_ADMIN or ROLE_BIURO can see all
            result = statusEnum != null 
                ? repo.findByStatus(statusEnum, pageable)
                : repo.findAll(pageable);
        } else {
            // ROLE_USER can only see own department
            if (currentUser.getDzial() == null) {
                throw new AccessDeniedException("User must be assigned to a department");
            }
            result = statusEnum != null 
                ? repo.findByDzialIdAndStatus(currentUser.getDzial().getId(), statusEnum, pageable)
                : repo.findByDzialId(currentUser.getDzial().getId(), pageable);
        }
        
        return toPage(result);
    }

    /**
     * Get single zgłoszenie with security check
     */
    public ZgloszenieDTO get(Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Zgloszenie z = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgłoszenie not found"));
        
        // Security check
        if (!hasFullAccess(currentUser) && !canAccessZgloszenie(z, currentUser)) {
            throw new AccessDeniedException("Access denied to this zgłoszenie");
        }
        
        return ZgloszenieMapper.toDto(z);
    }

    /**
     * Create new zgłoszenie
     */
    public ZgloszenieDTO create(ZgloszenieCreateRequest req, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        
        Zgloszenie z = new Zgloszenie();
        
        // Set basic fields
        z.setTyp(req.getTyp());
        z.setImie(req.getImie());
        z.setNazwisko(req.getNazwisko());
        z.setOpis(req.getOpis());
        z.setAutor(currentUser);
        
        // Map status
        ZgloszenieStatus mapped = ZgloszenieStatusMapper.map(req.getStatus());
        z.setStatus(mapped != null ? mapped : ZgloszenieStatus.OPEN);
        
        // Assign department logic
        Dzial dzial = determineDzial(req.getDzialId(), currentUser);
        z.setDzial(dzial);
        
        // TODO: Handle photo
        
        // Validation
        z.validate();
        
        repo.save(z);
        return ZgloszenieMapper.toDto(z);
    }

    /**
     * Update existing zgłoszenie
     */
    public ZgloszenieDTO update(Long id, ZgloszenieUpdateRequest req, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Zgloszenie z = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgłoszenie not found"));
        
        // Security check
        if (!hasFullAccess(currentUser) && !canAccessZgloszenie(z, currentUser)) {
            throw new AccessDeniedException("Access denied to this zgłoszenie");
        }
        
        // Update fields
        if (req.getTyp() != null) z.setTyp(req.getTyp());
        if (req.getOpis() != null) z.setOpis(req.getOpis());
        if (req.getStatus() != null) {
            ZgloszenieStatus ms = ZgloszenieStatusMapper.map(req.getStatus());
            if (ms != null) z.setStatus(ms);
        }
        
        // Validation
        z.validate();
        
        repo.save(z);
        return ZgloszenieMapper.toDto(z);
    }

    /**
     * Delete zgłoszenie
     */
    public void delete(Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Zgloszenie z = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgłoszenie not found"));
        
        // Security check
        if (!hasFullAccess(currentUser) && !canAccessZgloszenie(z, currentUser)) {
            throw new AccessDeniedException("Access denied to this zgłoszenie");
        }
        
        repo.deleteById(id);
    }

    // --- Helper methods ---
    
    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User must be authenticated");
        }
        
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("User not found: " + authentication.getName()));
    }
    
    private boolean hasFullAccess(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()) || "ROLE_BIURO".equals(role.getName()));
    }
    
    private boolean canAccessZgloszenie(Zgloszenie z, User user) {
        // User can access if it's in their department
        return user.getDzial() != null && user.getDzial().equals(z.getDzial());
    }
    
    private Dzial determineDzial(Long requestedDzialId, User currentUser) {
        if (hasFullAccess(currentUser)) {
            // ADMIN/BIURO can set dzialId or fallback to own department
            if (requestedDzialId != null) {
                return dzialRepository.findById(requestedDzialId)
                        .orElseThrow(() -> new IllegalArgumentException("Department not found: " + requestedDzialId));
            }
        }
        
        // Fallback to user's department (for USER or ADMIN/BIURO without dzialId)
        if (currentUser.getDzial() == null) {
            throw new IllegalArgumentException("User must be assigned to a department");
        }
        
        return currentUser.getDzial();
    }
    
    private ZgloszeniePage toPage(Page<Zgloszenie> page) {
        var content = page.getContent().stream()
                .map(ZgloszenieMapper::toDto)
                .collect(Collectors.toList());
        
        return new ZgloszeniePage(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSize(),
                page.getNumber()
        );
    }
}