package drimer.drimain.events;

import drimer.drimain.api.dto.ZgloszenieDTO;
import drimer.drimain.model.enums.ZgloszenieEventType;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domenowy event dla zmian w zgłoszeniach.
 * Wzorowany na RaportChangedEvent z dodatkowymi polami dla różnych typów eventów.
 */
public class ZgloszenieDomainEvent extends ApplicationEvent {
    
    private final ZgloszenieEventType type;
    private final Long zgloszenieId;
    private final LocalDateTime eventTimestamp;
    private final ZgloszenieDTO data; // opcjonalne - pełne dane lub null
    private final List<String> changedFields; // opcjonalne - tylko zmienione pola
    private final Long attachmentId; // dla eventów związanych z załącznikami

    public ZgloszenieDomainEvent(Object source, ZgloszenieEventType type, Long zgloszenieId, 
                                ZgloszenieDTO data) {
        this(source, type, zgloszenieId, data, null, null);
    }

    public ZgloszenieDomainEvent(Object source, ZgloszenieEventType type, Long zgloszenieId, 
                                ZgloszenieDTO data, List<String> changedFields, Long attachmentId) {
        super(source);
        this.type = type;
        this.zgloszenieId = zgloszenieId;
        this.data = data;
        this.changedFields = changedFields;
        this.attachmentId = attachmentId;
        this.eventTimestamp = LocalDateTime.now();
    }

    public ZgloszenieEventType getType() { return type; }
    public Long getZgloszenieId() { return zgloszenieId; }
    public LocalDateTime getEventTimestamp() { return eventTimestamp; }
    public ZgloszenieDTO getData() { return data; }
    public List<String> getChangedFields() { return changedFields; }
    public Long getAttachmentId() { return attachmentId; }
}