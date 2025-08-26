package drimer.drimain.api.dto;

/**
 * Request DTO for creating a new Zgloszenie.
 * dzialId is optional - if not provided, user's department will be used for regular users.
 */
public class ZgloszenieNewCreateRequest {
    
    private String tytul;
    private String opis;
    private Long dzialId; // Optional - for ADMIN/BIURO users

    // Constructors
    public ZgloszenieNewCreateRequest() {}

    public ZgloszenieNewCreateRequest(String tytul, String opis, Long dzialId) {
        this.tytul = tytul;
        this.opis = opis;
        this.dzialId = dzialId;
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

    public Long getDzialId() {
        return dzialId;
    }

    public void setDzialId(Long dzialId) {
        this.dzialId = dzialId;
    }
}