package drimer.drimain.api.dto;

import drimer.drimain.model.enums.ZgloszenieStatus;

/**
 * DTO for Zgloszenie entity according to new requirements.
 */
public class ZgloszenieNewDTO {
    
    private Long id;
    private String tytul;
    private String opis;
    private ZgloszenieStatus status;
    private Long dzialId;
    private String dzialNazwa;
    private String autorUsername;

    // Constructors
    public ZgloszenieNewDTO() {}

    public ZgloszenieNewDTO(Long id, String tytul, String opis, ZgloszenieStatus status, 
                        Long dzialId, String dzialNazwa, String autorUsername) {
        this.id = id;
        this.tytul = tytul;
        this.opis = opis;
        this.status = status;
        this.dzialId = dzialId;
        this.dzialNazwa = dzialNazwa;
        this.autorUsername = autorUsername;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getDzialId() {
        return dzialId;
    }

    public void setDzialId(Long dzialId) {
        this.dzialId = dzialId;
    }

    public String getDzialNazwa() {
        return dzialNazwa;
    }

    public void setDzialNazwa(String dzialNazwa) {
        this.dzialNazwa = dzialNazwa;
    }

    public String getAutorUsername() {
        return autorUsername;
    }

    public void setAutorUsername(String autorUsername) {
        this.autorUsername = autorUsername;
    }
}