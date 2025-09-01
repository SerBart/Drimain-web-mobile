package drimer.drimain.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * DTO for Attachment entity.
 * Used for API responses containing attachment metadata.
 */
public class AttachmentDTO {

    private Long id;
    private Long zgloszenieId;
    private String originalFilename;
    private String contentType;
    private Long size;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private String checksum;

    public AttachmentDTO() {}

    public AttachmentDTO(Long id, Long zgloszenieId, String originalFilename, 
                        String contentType, Long size, LocalDateTime createdAt, String checksum) {
        this.id = id;
        this.zgloszenieId = zgloszenieId;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.size = size;
        this.createdAt = createdAt;
        this.checksum = checksum;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getZgloszenieId() {
        return zgloszenieId;
    }

    public void setZgloszenieId(Long zgloszenieId) {
        this.zgloszenieId = zgloszenieId;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}