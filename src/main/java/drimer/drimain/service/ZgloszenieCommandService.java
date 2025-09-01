package drimer.drimain.service;

import drimer.drimain.api.dto.ZgloszenieCreateRequest;
import drimer.drimain.api.dto.ZgloszenieDTO;
import drimer.drimain.api.dto.ZgloszenieUpdateRequest;
import drimer.drimain.api.mapper.ZgloszenieMapper;
import drimer.drimain.events.ZgloszenieDomainEvent;
import drimer.drimain.model.Dzial;
import drimer.drimain.model.User;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieEventType;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.repository.DzialRepository;
import drimer.drimain.repository.UserRepository;
import drimer.drimain.repository.ZgloszenieRepository;
import drimer.drimain.util.ZgloszenieStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Serwis komend dla zgłoszeń - centralizuje operacje CRUD i publikację eventów.
 * Zastępuje bezpośrednie wywołania repository w kontrolerach.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ZgloszenieCommandService {

    private final ZgloszenieRepository zgloszenieRepository;
    private final DzialRepository dzialRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Tworzy nowe zgłoszenie i publikuje event CREATED.
     */
    public ZgloszenieDTO create(ZgloszenieCreateRequest req) {
        Zgloszenie z = new Zgloszenie();
        
        // Mapowanie podstawowych pól
        ZgloszenieStatus mapped = ZgloszenieStatusMapper.map(req.getStatus());
        z.setStatus(mapped != null ? mapped : ZgloszenieStatus.OPEN);
        z.setTyp(req.getTyp());
        z.setImie(req.getImie());
        z.setNazwisko(req.getNazwisko());
        z.setTytul(req.getTytul());
        z.setOpis(req.getOpis());
        z.setDataGodzina(req.getDataGodzina() != null ? req.getDataGodzina() : LocalDateTime.now());
        
        // Relacje
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
        
        // Zapisz i konwertuj na DTO
        Zgloszenie saved = zgloszenieRepository.save(z);
        ZgloszenieDTO dto = ZgloszenieMapper.toDto(saved);
        
        // Publikuj event
        eventPublisher.publishEvent(new ZgloszenieDomainEvent(
                this, ZgloszenieEventType.CREATED, saved.getId(), dto));
        
        return dto;
    }

    /**
     * Aktualizuje istniejące zgłoszenie i publikuje odpowiednie eventy.
     */
    public ZgloszenieDTO update(Long id, ZgloszenieUpdateRequest req) {
        Zgloszenie z = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found"));

        // Śledź zmienione pola
        List<String> changedFields = new ArrayList<>();
        ZgloszenieStatus originalStatus = z.getStatus();
        
        // Aktualizuj pola i śledź zmiany
        if (req.getTyp() != null && !req.getTyp().equals(z.getTyp())) {
            z.setTyp(req.getTyp());
            changedFields.add("typ");
        }
        if (req.getImie() != null && !req.getImie().equals(z.getImie())) {
            z.setImie(req.getImie());
            changedFields.add("imie");
        }
        if (req.getNazwisko() != null && !req.getNazwisko().equals(z.getNazwisko())) {
            z.setNazwisko(req.getNazwisko());
            changedFields.add("nazwisko");
        }
        if (req.getTytul() != null && !req.getTytul().equals(z.getTytul())) {
            z.setTytul(req.getTytul());
            changedFields.add("tytul");
        }
        if (req.getOpis() != null && !req.getOpis().equals(z.getOpis())) {
            z.setOpis(req.getOpis());
            changedFields.add("opis");
        }
        
        // Status - wydzielmy to osobno bo może generować dodatkowy event
        boolean statusChanged = false;
        if (req.getStatus() != null) {
            ZgloszenieStatus ms = ZgloszenieStatusMapper.map(req.getStatus());
            if (ms != null && !ms.equals(originalStatus)) {
                z.setStatus(ms);
                changedFields.add("status");
                statusChanged = true;
            }
        }
        
        // Relacje
        if (req.getDzialId() != null) {
            Dzial dzial = dzialRepository.findById(req.getDzialId())
                    .orElseThrow(() -> new IllegalArgumentException("Dzial not found"));
            if (!dzial.equals(z.getDzial())) {
                z.setDzial(dzial);
                changedFields.add("dzial");
            }
        }
        if (req.getAutorId() != null) {
            User autor = userRepository.findById(req.getAutorId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            if (!autor.equals(z.getAutor())) {
                z.setAutor(autor);
                changedFields.add("autor");
            }
        }
        
        // Zapisz i konwertuj
        Zgloszenie saved = zgloszenieRepository.save(z);
        ZgloszenieDTO dto = ZgloszenieMapper.toDto(saved);
        
        // Publikuj eventy
        if (!changedFields.isEmpty()) {
            eventPublisher.publishEvent(new ZgloszenieDomainEvent(
                    this, ZgloszenieEventType.UPDATED, saved.getId(), dto, changedFields, null));
        }
        
        // Dodatkowy event dla zmiany statusu (jeśli wymagane)
        if (statusChanged) {
            eventPublisher.publishEvent(new ZgloszenieDomainEvent(
                    this, ZgloszenieEventType.STATUS_CHANGED, saved.getId(), dto));
        }
        
        return dto;
    }

    /**
     * Usuwa zgłoszenie i publikuje event DELETED.
     */
    public void delete(Long id) {
        if (!zgloszenieRepository.existsById(id)) {
            throw new IllegalArgumentException("Zgloszenie not found");
        }
        
        // Pobierz dane przed usunięciem dla eventu
        Zgloszenie z = zgloszenieRepository.findById(id).orElse(null);
        ZgloszenieDTO dto = z != null ? ZgloszenieMapper.toDto(z) : null;
        
        // Usuń
        zgloszenieRepository.deleteById(id);
        
        // Publikuj event (z minimalnymi danymi)
        eventPublisher.publishEvent(new ZgloszenieDomainEvent(
                this, ZgloszenieEventType.DELETED, id, dto));
    }

    /**
     * Obsługa dodania załącznika (placeholder).
     */
    public void addAttachment(Long zgloszenieId, Long attachmentId) {
        if (!zgloszenieRepository.existsById(zgloszenieId)) {
            throw new IllegalArgumentException("Zgloszenie not found");
        }
        
        // TODO: Implementacja dodania załącznika
        
        eventPublisher.publishEvent(new ZgloszenieDomainEvent(
                this, ZgloszenieEventType.ATTACHMENT_ADDED, zgloszenieId, null, null, attachmentId));
    }

    /**
     * Obsługa usunięcia załącznika (placeholder).
     */
    public void removeAttachment(Long zgloszenieId, Long attachmentId) {
        if (!zgloszenieRepository.existsById(zgloszenieId)) {
            throw new IllegalArgumentException("Zgloszenie not found");
        }
        
        // TODO: Implementacja usunięcia załącznika
        
        eventPublisher.publishEvent(new ZgloszenieDomainEvent(
                this, ZgloszenieEventType.ATTACHMENT_REMOVED, zgloszenieId, null, null, attachmentId));
    }
}