package drimer.drimain.api.mapper;

import drimer.drimain.api.dto.ZgloszenieDTO;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.util.ZgloszenieStatusMapper;

public final class ZgloszenieMapper {
    private ZgloszenieMapper() {}

    public static ZgloszenieDTO toDto(Zgloszenie z) {
        ZgloszenieDTO dto = new ZgloszenieDTO();
        dto.setId(z.getId());
        
        // New fields
        dto.setTytul(z.getTytul());
        dto.setOpis(z.getOpis());
        dto.setStatus(ZgloszenieStatusMapper.toString(z.getStatus()));
        dto.setCreatedAt(z.getCreatedAt());
        dto.setUpdatedAt(z.getUpdatedAt());
        
        if (z.getDzial() != null) {
            dto.setDzialId(z.getDzial().getId());
            dto.setDzialNazwa(z.getDzial().getNazwa());
        }
        
        if (z.getAutor() != null) {
            dto.setAutorUsername(z.getAutor().getUsername());
        }
        
        // Legacy fields
        dto.setTyp(z.getTyp());
        dto.setImie(z.getImie());
        dto.setNazwisko(z.getNazwisko());
        dto.setDataGodzina(z.getDataGodzina());
        
        return dto;
    }

    public static void updateEntity(Zgloszenie z, ZgloszenieDTO dto) {
        // Legacy update
        z.setTyp(dto.getTyp());
        z.setImie(dto.getImie());
        z.setNazwisko(dto.getNazwisko());
        z.setOpis(dto.getOpis());
        z.setDataGodzina(dto.getDataGodzina());
        
        // New fields
        if (dto.getTytul() != null) z.setTytul(dto.getTytul());
        if (dto.getStatus() != null) {
            z.setStatus(ZgloszenieStatusMapper.map(dto.getStatus()));
        }
    }
}