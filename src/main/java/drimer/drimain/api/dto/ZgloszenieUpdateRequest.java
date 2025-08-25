package drimer.drimain.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ZgloszenieUpdateRequest {
    private String typ;
    private String imie;
    private String nazwisko;
    private String status;
    private String opis;
    private LocalDateTime dataGodzina;
    private String photoBase64; // opcjonalnie
}