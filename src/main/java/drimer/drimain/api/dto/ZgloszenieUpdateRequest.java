package drimer.drimain.api.dto;

import lombok.Data;

@Data
public class ZgloszenieUpdateRequest {
    private String tytul;
    private String status;
    private String opis;
}