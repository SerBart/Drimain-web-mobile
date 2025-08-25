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
        return dto;
    }

    public static void updateEntity(Zgloszenie z, ZgloszenieDTO dto) {
        z.setTyp(dto.getTyp());
        z.setImie(dto.getImie());
        z.setNazwisko(dto.getNazwisko());
        z.setStatus(dto.getStatus());
        z.setOpis(dto.getOpis());
        z.setDataGodzina(dto.getDataGodzina());
    }
}