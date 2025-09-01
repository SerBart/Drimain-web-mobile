package drimer.drimain.repository;

import drimer.drimain.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    
    // Find attachments by zgloszenie id
    List<Attachment> findByZgloszenieId(Long zgloszenieId);
    
    // Find attachment by stored filename
    Attachment findByStoredFilename(String storedFilename);
    
    // Count attachments for a zgloszenie
    long countByZgloszenieId(Long zgloszenieId);
}