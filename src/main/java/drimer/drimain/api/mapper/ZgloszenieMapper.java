package drimer.drimain.api.mapper;

import drimer.drimain.api.dto.ZgloszenieDTO;
import drimer.drimain.model.Zgloszenie;

public final class ZgloszenieMapper {
    private ZgloszenieMapper() {}

    public static ZgloszenieDTO toDto(Zgloszenie z) {
        ZgloszenieDTO dto = new ZgloszenieDTO();
        dto.setId(z.getId());
        dto.setTytul(z.getTytul());
        dto.setOpis(z.getOpis());
        dto.setStatus(z.getStatus());
        dto.setCreatedAt(z.getCreatedAt());
        dto.setUpdatedAt(z.getUpdatedAt());
        
        if (z.getDzial() != null) {
            dto.setDzialId(z.getDzial().getId());
            dto.setDzialNazwa(z.getDzial().getNazwa());
        }
        
        if (z.getAutor() != null) {
            dto.setAutorUsername(z.getAutor().getUsername());
        }
        
        return dto;
    }

    public static void updateEntity(Zgloszenie z, ZgloszenieDTO dto) {
        z.setTytul(dto.getTytul());
        z.setOpis(dto.getOpis());
        z.setStatus(dto.getStatus());
        // Note: department and author are not updated via DTO for security reasons
    }
}