package drimer.drimain.api.dto;

import lombok.Data;

@Data
public class ZgloszenieUpdateRequest {
    private String tytul;
    private String opis;
    private String status;
    
    // Legacy fields for backward compatibility
    private String typ;
    private String imie;
    private String nazwisko;
    private String photoBase64; // opcjonalnie
}