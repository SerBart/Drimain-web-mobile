package drimer.drimain.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import drimer.drimain.model.enums.ZgloszenieEventType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO dla eventów zgłoszeń przesyłanych przez SSE.
 */
public class ZgloszenieEventDTO {
    
    private ZgloszenieEventType type;
    private Long zgloszenieId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    private ZgloszenieDTO data; // opcjonalne - pełne dane zgłoszenia
    private List<String> changedFields; // opcjonalne - lista zmienionych pól
    private Long attachmentId; // dla eventów załączników

    public ZgloszenieEventDTO() {}

    public ZgloszenieEventDTO(ZgloszenieEventType type, Long zgloszenieId, LocalDateTime timestamp,
                             ZgloszenieDTO data, List<String> changedFields, Long attachmentId) {
        this.type = type;
        this.zgloszenieId = zgloszenieId;
        this.timestamp = timestamp;
        this.data = data;
        this.changedFields = changedFields;
        this.attachmentId = attachmentId;
    }

    public ZgloszenieEventType getType() { return type; }
    public void setType(ZgloszenieEventType type) { this.type = type; }

    public Long getZgloszenieId() { return zgloszenieId; }
    public void setZgloszenieId(Long zgloszenieId) { this.zgloszenieId = zgloszenieId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public ZgloszenieDTO getData() { return data; }
    public void setData(ZgloszenieDTO data) { this.data = data; }

    public List<String> getChangedFields() { return changedFields; }
    public void setChangedFields(List<String> changedFields) { this.changedFields = changedFields; }

    public Long getAttachmentId() { return attachmentId; }
    public void setAttachmentId(Long attachmentId) { this.attachmentId = attachmentId; }
}