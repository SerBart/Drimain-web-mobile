package drimer.drimain.api.dto;

import lombok.Data;

@Data
public class ZgloszenieCreateRequest {
    private String tytul;
    private String opis;
    private String status;
    private Long dzialId; // Optional - will be set to user's department if not provided
    
    // Legacy fields for backward compatibility
    private String typ;
    private String imie;
    private String nazwisko;
    private String photoBase64; // opcjonalnie
}