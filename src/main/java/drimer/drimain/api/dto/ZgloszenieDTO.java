package drimer.drimain.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ZgloszenieDTO {
    private Long id;
    private String tytul;
    private String opis;
    private String status;
    private Long dzialId;
    private String dzialNazwa;
    private String autorUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Legacy fields for backward compatibility
    private String typ;
    private String imie;
    private String nazwisko;
    private LocalDateTime dataGodzina;
}