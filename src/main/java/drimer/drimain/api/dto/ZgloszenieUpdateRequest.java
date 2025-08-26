package drimer.drimain.api.dto;

import lombok.Data;

@Data
public class ZgloszenieUpdateRequest {
    private String typ;
    private String imie;
    private String nazwisko;
    private String tytul; // New field
    private String status;
    private String opis;
    private Long dzialId; // New field
    private Long autorId; // New field
    private String photoBase64; // opcjonalnie
}