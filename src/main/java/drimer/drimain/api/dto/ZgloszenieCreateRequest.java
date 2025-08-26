package drimer.drimain.api.dto;

import lombok.Data;

@Data
public class ZgloszenieCreateRequest {
    private String tytul;
    private String opis;
    private String status; // Optional, defaults to NEW
    private Long dzialId; // Optional, only for ADMIN/BIURO
}