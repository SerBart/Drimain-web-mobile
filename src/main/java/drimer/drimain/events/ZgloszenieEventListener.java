package drimer.drimain.events;

import drimer.drimain.api.dto.ZgloszenieEventDTO;
import drimer.drimain.service.ZgloszenieSSEService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener eventów zgłoszeń - konwertuje domenowe eventy na DTOs i rozgłasza przez SSE.
 */
@Component
@RequiredArgsConstructor
public class ZgloszenieEventListener {

    private static final Logger log = LoggerFactory.getLogger(ZgloszenieEventListener.class);
    
    private final ZgloszenieSSEService sseService;

    @EventListener
    public void onZgloszenieChanged(ZgloszenieDomainEvent event) {
        log.debug("Processing ZgloszenieDomainEvent: type={}, id={}", event.getType(), event.getZgloszenieId());
        
        // Konwertuj domenowy event na DTO
        ZgloszenieEventDTO eventDTO = new ZgloszenieEventDTO(
                event.getType(),
                event.getZgloszenieId(),
                event.getEventTimestamp(),
                event.getData(),
                event.getChangedFields(),
                event.getAttachmentId()
        );
        
        // Rozgłoś przez SSE
        sseService.broadcast(eventDTO);
    }
}