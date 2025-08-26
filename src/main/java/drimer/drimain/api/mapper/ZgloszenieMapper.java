package drimer.drimain.api.mapper;

import drimer.drimain.api.dto.ZgloszenieDTO;
import drimer.drimain.model.Zgloszenie;

public final class ZgloszenieMapper {
    private ZgloszenieMapper() {}

    public static ZgloszenieDTO toDto(Zgloszenie z) {
        ZgloszenieDTO dto = new ZgloszenieDTO();
        dto.setId(z.getId());
        dto.setTyp(z.getTyp());
        dto.setImie(z.getImie());
        dto.setNazwisko(z.getNazwisko());
        dto.setStatus(z.getStatus());
        dto.setOpis(z.getOpis());
        dto.setDataGodzina(z.getDataGodzina());
        dto.setCreatedAt(z.getCreatedAt());
        dto.setUpdatedAt(z.getUpdatedAt());
        
        // Department info
        if (z.getDzial() != null) {
            dto.setDzialId(z.getDzial().getId());
            dto.setDzialNazwa(z.getDzial().getNazwa());
        }
        
        // Author info
        if (z.getAutor() != null) {
            dto.setAutorUsername(z.getAutor().getUsername());
        }
        
        // TODO: implement photo detection logic
        dto.setHasPhoto(false);
        
        return dto;
    }

    public static void updateEntity(Zgloszenie z, ZgloszenieDTO dto) {
        z.setTyp(dto.getTyp());
        z.setImie(dto.getImie());
        z.setNazwisko(dto.getNazwisko());
        z.setStatus(dto.getStatus());
        z.setOpis(dto.getOpis());
        z.setDataGodzina(dto.getDataGodzina());
        // Note: createdAt, updatedAt, dzial, and autor are managed separately
        // and should not be updated via DTO mapping for security reasons
    }
}