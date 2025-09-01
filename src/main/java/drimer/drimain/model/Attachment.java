package drimer.drimain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing file attachments associated with Zgloszenie.
 * Supports physical file storage with metadata tracking.
 */
@Entity
@Table(name = "attachments", indexes = {
    @Index(name = "idx_attachment_zgloszenie", columnList = "zgloszenie_id")
})
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zgloszenie_id", nullable = false)
    private Zgloszenie zgloszenie;

    @Column(name = "original_filename", nullable = false, length = 500)
    private String originalFilename;

    @Column(name = "stored_filename", nullable = false, unique = true, length = 500)
    private String storedFilename;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private Long size;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Optional SHA-256 checksum for file integrity
    @Column(name = "checksum", length = 64)
    private String checksum;

    // JPA lifecycle methods
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Default constructor
    public Attachment() {}

    // Constructor for creating new attachments
    public Attachment(Zgloszenie zgloszenie, String originalFilename, String storedFilename, 
                     String contentType, Long size) {
        this.zgloszenie = zgloszenie;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.contentType = contentType;
        this.size = size;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Zgloszenie getZgloszenie() {
        return zgloszenie;
    }

    public void setZgloszenie(Zgloszenie zgloszenie) {
        this.zgloszenie = zgloszenie;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
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