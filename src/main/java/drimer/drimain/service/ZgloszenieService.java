package drimer.drimain.service;

import drimer.drimain.api.dto.ZgloszenieCreateRequest;
import drimer.drimain.api.dto.ZgloszenieUpdateRequest;
import drimer.drimain.model.Dzial;
import drimer.drimain.model.User;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.repository.DzialRepository;
import drimer.drimain.repository.ZgloszenieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ZgloszenieService {

    private final ZgloszenieRepository repo;
    private final DzialRepository dzialRepository;

    /**
     * List zgłoszenia for user with proper department-based filtering
     */
    public Page<Zgloszenie> listForUser(User user, ZgloszenieStatus status, Pageable pageable, 
                                       boolean isAdmin, boolean isBiuro) {
        // ADMIN and BIURO see ALL zgłoszenia
        if (isAdmin || isBiuro) {
            return repo.findByOptionalStatus(status, pageable);
        }
        
        // Regular users see only their department's zgłoszenia
        if (user.getDzial() == null) {
            // User without department sees no zgłoszenia
            return Page.empty(pageable);
        }
        
        return repo.findByDzialIdAndOptionalStatus(user.getDzial().getId(), status, pageable);
    }

    /**
     * Create new zgłoszenie with department-based logic
     */
    public Zgloszenie create(ZgloszenieCreateRequest req, User currentUser, boolean isAdmin, boolean isBiuro) {
        Zgloszenie z = new Zgloszenie();

        z.setTytul(req.getTytul());
        z.setOpis(req.getOpis());
        z.setStatus(req.getStatus() != null ? ZgloszenieStatus.valueOf(req.getStatus()) : ZgloszenieStatus.NEW);
        z.setAutor(currentUser);
        z.setCreatedAt(LocalDateTime.now());
        z.setUpdatedAt(LocalDateTime.now());

        // Department assignment logic
        if (req.getDzialId() != null && (isAdmin || isBiuro)) {
            // ADMIN/BIURO can specify department (validate exists)
            Dzial dzial = dzialRepository.findById(req.getDzialId())
                    .orElseThrow(() -> new IllegalArgumentException("Dział nie istnieje"));
            z.setDzial(dzial);
        } else {
            // Non-admin & non-BIURO users forced to their own department
            if (currentUser.getDzial() == null) {
                throw new IllegalArgumentException("Użytkownik nie ma przypisanego działu");
            }
            z.setDzial(currentUser.getDzial());
        }

        z.validate();
        return repo.save(z);
    }

    /**
     * Update zgłoszenie with access control
     */
    public Zgloszenie update(Long id, ZgloszenieUpdateRequest req, User currentUser, 
                           boolean isAdmin, boolean isBiuro) {
        Zgloszenie z = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgłoszenie nie znalezione"));
        
        // Check access rights
        if (!canAccessZgloszenie(z, currentUser, isAdmin, isBiuro)) {
            throw new AccessDeniedException("Brak dostępu do tego zgłoszenia");
        }

        if (req.getTytul() != null) z.setTytul(req.getTytul());
        if (req.getStatus() != null) z.setStatus(ZgloszenieStatus.valueOf(req.getStatus()));
        if (req.getOpis() != null) z.setOpis(req.getOpis());
        z.setUpdatedAt(LocalDateTime.now());

        z.validate();
        return repo.save(z);
    }

    /**
     * Get single zgłoszenie with access control
     */
    public Zgloszenie get(Long id, User currentUser, boolean isAdmin, boolean isBiuro) {
        Zgloszenie z = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgłoszenie nie znalezione"));
        
        if (!canAccessZgloszenie(z, currentUser, isAdmin, isBiuro)) {
            throw new AccessDeniedException("Brak dostępu do tego zgłoszenia");
        }
        
        return z;
    }

    /**
     * Delete zgłoszenie with access control
     */
    public void delete(Long id, User currentUser, boolean isAdmin, boolean isBiuro) {
        Zgloszenie z = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgłoszenie nie znalezione"));
        
        if (!canAccessZgloszenie(z, currentUser, isAdmin, isBiuro)) {
            throw new AccessDeniedException("Brak dostępu do tego zgłoszenia");
        }
        
        repo.deleteById(id);
    }

    /**
     * Check if user can access specific zgłoszenie
     */
    private boolean canAccessZgloszenie(Zgloszenie zgloszenie, User user, boolean isAdmin, boolean isBiuro) {
        // ADMIN and BIURO can access all zgłoszenia
        if (isAdmin || isBiuro) {
            return true;
        }
        
        // Regular users can only access zgłoszenia from their department
        return user.getDzial() != null && 
               zgloszenie.getDzial() != null && 
               user.getDzial().getId().equals(zgloszenie.getDzial().getId());
    }
}