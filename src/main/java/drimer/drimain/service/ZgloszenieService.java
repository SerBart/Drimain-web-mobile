package drimer.drimain.service;

import drimer.drimain.api.dto.ZgloszenieNewDTO;
import drimer.drimain.api.dto.ZgloszenieNewCreateRequest;
import drimer.drimain.api.dto.ZgloszenieNewUpdateRequest;
import drimer.drimain.model.Dzial;
import drimer.drimain.model.User;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.repository.DzialRepository;
import drimer.drimain.repository.UserRepository;
import drimer.drimain.repository.ZgloszenieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ZgloszenieService {

    private final ZgloszenieRepository zgloszenieRepository;
    private final DzialRepository dzialRepository;
    private final UserRepository userRepository;

    /**
     * List zgloszenia with access control rules.
     * ADMIN/BIURO can view all; regular users limited to their department.
     */
    public Page<ZgloszenieNewDTO> list(Pageable pageable, ZgloszenieStatus status, Authentication auth) {
        User currentUser = getCurrentUser(auth);
        
        if (isAdminOrBiuro(auth)) {
            // ADMIN/BIURO can view all
            Page<Zgloszenie> zgloszenia = (status != null) 
                ? zgloszenieRepository.findByStatus(status, pageable)
                : zgloszenieRepository.findAll(pageable);
            return zgloszenia.map(this::toDTO);
        } else {
            // Regular users limited to their department
            if (currentUser.getDzial() == null) {
                throw new IllegalStateException("User has no department assigned");
            }
            
            Page<Zgloszenie> zgloszenia = (status != null)
                ? zgloszenieRepository.findByDzial_IdAndStatus(currentUser.getDzial().getId(), status, pageable)
                : zgloszenieRepository.findByDzial_Id(currentUser.getDzial().getId(), pageable);
            return zgloszenia.map(this::toDTO);
        }
    }

    /**
     * Create new zgloszenie with access control.
     */
    @Transactional
    public ZgloszenieNewDTO create(ZgloszenieNewCreateRequest req, Authentication auth) {
        User currentUser = getCurrentUser(auth);
        
        Zgloszenie zgloszenie = new Zgloszenie();
        zgloszenie.setTytul(req.getTytul());
        zgloszenie.setOpis(req.getOpis());
        zgloszenie.setStatus(ZgloszenieStatus.NEW);
        zgloszenie.setAutor(currentUser);
        
        // Determine dzial
        if (isAdminOrBiuro(auth)) {
            // ADMIN/BIURO may specify dzialId or use their own
            if (req.getDzialId() != null) {
                Dzial dzial = dzialRepository.findById(req.getDzialId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found: " + req.getDzialId()));
                zgloszenie.setDzial(dzial);
            } else if (currentUser.getDzial() != null) {
                zgloszenie.setDzial(currentUser.getDzial());
            } else {
                throw new IllegalArgumentException("No department specified and user has no department assigned");
            }
        } else {
            // Regular users must use their own department
            if (currentUser.getDzial() == null) {
                throw new IllegalStateException("User has no department assigned");
            }
            zgloszenie.setDzial(currentUser.getDzial());
        }
        
        Zgloszenie saved = zgloszenieRepository.save(zgloszenie);
        return toDTO(saved);
    }

    /**
     * Get zgloszenie by ID with access control.
     */
    public ZgloszenieNewDTO get(Long id, Authentication auth) {
        Zgloszenie zgloszenie = zgloszenieRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found: " + id));
        
        checkReadAccess(zgloszenie, auth);
        return toDTO(zgloszenie);
    }

    /**
     * Update zgloszenie with access control.
     */
    @Transactional
    public ZgloszenieNewDTO update(Long id, ZgloszenieNewUpdateRequest req, Authentication auth) {
        Zgloszenie zgloszenie = zgloszenieRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found: " + id));
        
        checkWriteAccess(zgloszenie, auth);
        
        if (req.getTytul() != null) {
            zgloszenie.setTytul(req.getTytul());
        }
        if (req.getOpis() != null) {
            zgloszenie.setOpis(req.getOpis());
        }
        if (req.getStatus() != null) {
            zgloszenie.setStatus(req.getStatus());
        }
        
        Zgloszenie updated = zgloszenieRepository.save(zgloszenie);
        return toDTO(updated);
    }

    /**
     * Delete zgloszenie with access control.
     */
    @Transactional
    public void delete(Long id, Authentication auth) {
        Zgloszenie zgloszenie = zgloszenieRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found: " + id));
        
        checkWriteAccess(zgloszenie, auth);
        zgloszenieRepository.deleteById(id);
    }

    // Helper methods
    
    private User getCurrentUser(Authentication auth) {
        return userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new IllegalStateException("Current user not found: " + auth.getName()));
    }

    private boolean isAdminOrBiuro(Authentication auth) {
        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
            || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_BIURO"));
    }

    private void checkReadAccess(Zgloszenie zgloszenie, Authentication auth) {
        if (isAdminOrBiuro(auth)) {
            return; // Unrestricted access
        }
        
        User currentUser = getCurrentUser(auth);
        if (currentUser.getDzial() == null || !currentUser.getDzial().getId().equals(zgloszenie.getDzial().getId())) {
            throw new AccessDeniedException("Access denied to zgloszenie from different department");
        }
    }

    private void checkWriteAccess(Zgloszenie zgloszenie, Authentication auth) {
        checkReadAccess(zgloszenie, auth); // Same rules for now
    }

    private ZgloszenieNewDTO toDTO(Zgloszenie zgloszenie) {
        return new ZgloszenieNewDTO(
            zgloszenie.getId(),
            zgloszenie.getTytul(),
            zgloszenie.getOpis(),
            zgloszenie.getStatus(),
            zgloszenie.getDzial().getId(),
            zgloszenie.getDzial().getNazwa(),
            zgloszenie.getAutor().getUsername()
        );
    }
}