package drimer.drimain.service;

import drimer.drimain.api.dto.PageDTO;
import drimer.drimain.api.dto.ZgloszenieCreateRequest;
import drimer.drimain.api.dto.ZgloszenieDTO;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZgloszenieService {

    private final ZgloszenieRepository zgloszenieRepository;
    private final UserRepository userRepository;
    private final DzialRepository dzialRepository;

    public PageDTO<ZgloszenieDTO> findAll(String username, List<String> userRoles, ZgloszenieStatus status, Pageable pageable) {
        Page<Zgloszenie> page;
        
        if (hasGlobalAccess(userRoles)) {
            // ADMIN or BIURO can see all
            page = zgloszenieRepository.findByStatusOrAll(status, pageable);
        } else {
            // Regular users see only their department's zgłoszenia
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
            if (user.getDzial() == null) {
                throw new AccessDeniedException("User has no department assigned");
            }
            
            if (status != null) {
                page = zgloszenieRepository.findByDzialIdAndStatus(user.getDzial().getId(), status, pageable);
            } else {
                page = zgloszenieRepository.findByDzialId(user.getDzial().getId(), pageable);
            }
        }
        
        return convertToPageDTO(page);
    }

    public ZgloszenieDTO findById(Long id, String username, List<String> userRoles) {
        Zgloszenie zgloszenie = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgłoszenie not found"));
        
        // Check access
        if (!hasAccessToZgloszenie(zgloszenie, username, userRoles)) {
            throw new AccessDeniedException("Access denied to this zgłoszenie");
        }
        
        return ZgloszenieMapper.toDto(zgloszenie);
    }

    public ZgloszenieDTO create(ZgloszenieCreateRequest request, String username, List<String> userRoles) {
        User autor = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Zgloszenie zgloszenie = new Zgloszenie();
        zgloszenie.setTytul(request.getTytul());
        zgloszenie.setOpis(request.getOpis());
        zgloszenie.setAutor(autor);
        
        // Set status
        ZgloszenieStatus status = request.getStatus() != null 
                ? ZgloszenieStatusMapper.map(request.getStatus())
                : ZgloszenieStatus.NEW;
        zgloszenie.setStatus(status != null ? status : ZgloszenieStatus.NEW);

        // Determine department
        if (request.getDzialId() != null && hasGlobalAccess(userRoles)) {
            // ADMIN or BIURO can specify department
            Dzial dzial = dzialRepository.findById(request.getDzialId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found"));
            zgloszenie.setDzial(dzial);
        } else {
            // Non-admin/biuro users are forced to their department
            if (autor.getDzial() == null) {
                throw new IllegalArgumentException("User has no department assigned");
            }
            zgloszenie.setDzial(autor.getDzial());
        }

        // Set legacy fields for backward compatibility
        zgloszenie.setTyp(request.getTyp());
        zgloszenie.setImie(request.getImie());
        zgloszenie.setNazwisko(request.getNazwisko());

        zgloszenie.validate();
        Zgloszenie saved = zgloszenieRepository.save(zgloszenie);
        return ZgloszenieMapper.toDto(saved);
    }

    public ZgloszenieDTO update(Long id, ZgloszenieUpdateRequest request, String username, List<String> userRoles) {
        Zgloszenie zgloszenie = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgłoszenie not found"));

        // Check access
        if (!hasAccessToZgloszenie(zgloszenie, username, userRoles)) {
            throw new AccessDeniedException("Access denied to this zgłoszenie");
        }

        // Update fields
        if (request.getTytul() != null) {
            zgloszenie.setTytul(request.getTytul());
        }
        if (request.getOpis() != null) {
            zgloszenie.setOpis(request.getOpis());
        }
        if (request.getStatus() != null) {
            ZgloszenieStatus status = ZgloszenieStatusMapper.map(request.getStatus());
            if (status != null) {
                zgloszenie.setStatus(status);
            }
        }

        // Legacy fields
        if (request.getTyp() != null) {
            zgloszenie.setTyp(request.getTyp());
        }
        if (request.getImie() != null) {
            zgloszenie.setImie(request.getImie());
        }
        if (request.getNazwisko() != null) {
            zgloszenie.setNazwisko(request.getNazwisko());
        }

        Zgloszenie saved = zgloszenieRepository.save(zgloszenie);
        return ZgloszenieMapper.toDto(saved);
    }

    public void delete(Long id, String username, List<String> userRoles) {
        Zgloszenie zgloszenie = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgłoszenie not found"));

        // Check access
        if (!hasAccessToZgloszenie(zgloszenie, username, userRoles)) {
            throw new AccessDeniedException("Access denied to this zgłoszenie");
        }

        zgloszenieRepository.delete(zgloszenie);
    }

    private boolean hasGlobalAccess(List<String> userRoles) {
        return userRoles.contains("ROLE_ADMIN") || userRoles.contains("ROLE_BIURO");
    }

    private boolean hasAccessToZgloszenie(Zgloszenie zgloszenie, String username, List<String> userRoles) {
        if (hasGlobalAccess(userRoles)) {
            return true;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user.getDzial() != null && 
               user.getDzial().getId().equals(zgloszenie.getDzial().getId());
    }

    private PageDTO<ZgloszenieDTO> convertToPageDTO(Page<Zgloszenie> page) {
        PageDTO<ZgloszenieDTO> pageDTO = new PageDTO<>();
        pageDTO.setContent(page.getContent().stream()
                .map(ZgloszenieMapper::toDto)
                .toList());
        pageDTO.setPage(page.getNumber());
        pageDTO.setSize(page.getSize());
        pageDTO.setTotalElements(page.getTotalElements());
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setFirst(page.isFirst());
        pageDTO.setLast(page.isLast());
        pageDTO.setNumberOfElements(page.getNumberOfElements());
        return pageDTO;
    }
}