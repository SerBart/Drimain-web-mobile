package drimer.drimain.api.dto;

import drimer.drimain.model.enums.ZgloszenieStatus;

/**
 * Request DTO for updating an existing Zgloszenie.
 * All fields are optional (nullable).
 */
public class ZgloszenieNewUpdateRequest {
    
    private String tytul;
    private String opis;
    private ZgloszenieStatus status;

    // Constructors
    public ZgloszenieNewUpdateRequest() {}

    public ZgloszenieNewUpdateRequest(String tytul, String opis, ZgloszenieStatus status) {
        this.tytul = tytul;
        this.opis = opis;
        this.status = status;
    }

    // Getters and Setters
    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public ZgloszenieStatus getStatus() {
        return status;
    }

    public void setStatus(ZgloszenieStatus status) {
        this.status = status;
    }
}