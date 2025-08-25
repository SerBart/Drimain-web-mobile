package drimer.drimain.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import drimer.drimain.model.enums.ZgloszenieStatus;

import java.time.LocalDateTime;

/**
 * DTO dla encji Zgloszenie.
 * dataGodzina jako LocalDateTime – Jackson serializuje do ISO (konfiguracja domyślna) lub wg @JsonFormat.
 */
public class ZgloszenieDTO {

    private Long id;
    private String typ;
    private String imie;
    private String nazwisko;
    private ZgloszenieStatus status;
    private String opis;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataGodzina;

    private boolean hasPhoto; // NOWE POLE

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTyp() { return typ; }
    public void setTyp(String typ) { this.typ = typ; }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public ZgloszenieStatus getStatus() { return status; }
    public void setStatus(ZgloszenieStatus status) { this.status = status; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public LocalDateTime getDataGodzina() { return dataGodzina; }
    public void setDataGodzina(LocalDateTime dataGodzina) { this.dataGodzina = dataGodzina; }

    public boolean isHasPhoto() { return hasPhoto; }
    public void setHasPhoto(boolean hasPhoto) { this.hasPhoto = hasPhoto; }
}