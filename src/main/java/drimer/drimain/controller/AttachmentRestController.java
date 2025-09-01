package drimer.drimain.controller;

import drimer.drimain.api.dto.AttachmentDTO;
import drimer.drimain.api.mapper.AttachmentMapper;
import drimer.drimain.model.Attachment;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.repository.AttachmentRepository;
import drimer.drimain.repository.ZgloszenieRepository;
import drimer.drimain.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing attachments.
 * Provides endpoints for upload, list, download, and delete operations.
 */
@RestController
@RequestMapping("/api/zgloszenia/{zgloszenieId}/attachments")
@RequiredArgsConstructor
public class AttachmentRestController {

    private final AttachmentRepository attachmentRepository;
    private final ZgloszenieRepository zgloszenieRepository;
    private final FileStorageService fileStorageService;

    // Maximum file size: 10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * Upload a file attachment for a zgloszenie.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AttachmentDTO uploadAttachment(@PathVariable Long zgloszenieId,
                                         @RequestParam("file") MultipartFile file,
                                         Authentication authentication) throws IOException {
        // Check if user is authenticated
        if (authentication == null) {
            throw new SecurityException("Authentication required");
        }

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size cannot exceed 10MB");
        }

        // Check if zgloszenie exists
        Zgloszenie zgloszenie = zgloszenieRepository.findById(zgloszenieId)
                .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found"));

        // Store the file
        String storedFilename = fileStorageService.storeFile(file);
        String checksum = fileStorageService.calculateChecksum(file);

        // Create attachment entity
        Attachment attachment = new Attachment(
                zgloszenie,
                file.getOriginalFilename(),
                storedFilename,
                file.getContentType(),
                file.getSize()
        );
        attachment.setChecksum(checksum);

        // Save attachment
        attachment = attachmentRepository.save(attachment);

        return AttachmentMapper.toDto(attachment);
    }

    /**
     * List all attachments for a zgloszenie.
     */
    @GetMapping
    public List<AttachmentDTO> listAttachments(@PathVariable Long zgloszenieId) {
        // Check if zgloszenie exists
        if (!zgloszenieRepository.existsById(zgloszenieId)) {
            throw new IllegalArgumentException("Zgloszenie not found");
        }

        return attachmentRepository.findByZgloszenieId(zgloszenieId)
                .stream()
                .map(AttachmentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Download an attachment file.
     */
    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long zgloszenieId,
                                                      @PathVariable Long attachmentId) throws IOException {
        // Find attachment
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found"));

        // Verify attachment belongs to the zgloszenie
        if (!attachment.getZgloszenie().getId().equals(zgloszenieId)) {
            throw new IllegalArgumentException("Attachment does not belong to this zgloszenie");
        }

        // Load file
        Path filePath = fileStorageService.loadFile(attachment.getStoredFilename());
        if (!fileStorageService.fileExists(attachment.getStoredFilename())) {
            throw new IllegalArgumentException("File not found");
        }

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                       "attachment; filename=\"" + attachment.getOriginalFilename() + "\"")
                .body(resource);
    }

    /**
     * Delete an attachment.
     */
    @DeleteMapping("/{attachmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttachment(@PathVariable Long zgloszenieId,
                                @PathVariable Long attachmentId,
                                Authentication authentication) {
        // Check if user has edit permissions (ADMIN or BIURO roles)
        if (!hasEditPermissions(authentication)) {
            throw new SecurityException("Access denied. Admin or Biuro role required.");
        }

        // Find attachment
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found"));

        // Verify attachment belongs to the zgloszenie
        if (!attachment.getZgloszenie().getId().equals(zgloszenieId)) {
            throw new IllegalArgumentException("Attachment does not belong to this zgloszenie");
        }

        // Delete physical file
        fileStorageService.deleteFile(attachment.getStoredFilename());

        // Delete attachment record
        attachmentRepository.delete(attachment);
    }

    /**
     * Check if the authenticated user has edit/delete permissions (ADMIN or BIURO role)
     */
    private boolean hasEditPermissions(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || 
                              a.getAuthority().equals("ROLE_BIURO"));
    }
}