package drimer.drimain.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ZgloszenieDTO {
    private Long id;
    private String typ;
    private String imie;
    private String nazwisko;
    private String status; // nazwa enum (tekst)
    private String opis;

    // get/set ...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTyp() { return typ; }
    public void setTyp(String typ) { this.typ = typ; }
    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }
    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

}