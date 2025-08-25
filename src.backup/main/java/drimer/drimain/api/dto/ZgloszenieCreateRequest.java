package drimer.drimain.api.dto;

import lombok.Data;

@Data
public class ZgloszenieCreateRequest {
    private String typ;
    private String imie;
    private String nazwisko;
    private String status;
    private String opis;
    private String photoBase64; // opcjonalnie
}