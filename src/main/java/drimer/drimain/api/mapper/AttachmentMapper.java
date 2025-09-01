package drimer.drimain.api.mapper;

import drimer.drimain.api.dto.AttachmentDTO;
import drimer.drimain.model.Attachment;

public final class AttachmentMapper {
    private AttachmentMapper() {}

    public static AttachmentDTO toDto(Attachment attachment) {
        if (attachment == null) {
            return null;
        }
        
        return new AttachmentDTO(
            attachment.getId(),
            attachment.getZgloszenie().getId(),
            attachment.getOriginalFilename(),
            attachment.getContentType(),
            attachment.getSize(),
            attachment.getCreatedAt(),
            attachment.getChecksum()
        );
    }
}