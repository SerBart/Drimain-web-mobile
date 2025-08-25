package drimer.drimain.api.dto;

import lombok.Data;

@Data
public class ZgloszenieUpdateRequest {
    private String typ;
    private String status;
    private String opis;
    private String photoBase64; // opcjonalnie
}