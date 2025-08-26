package drimer.drimain.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import drimer.drimain.model.enums.ZgloszenieStatus;

import java.time.LocalDateTime;

/**
 * DTO dla encji Zgloszenie z informacjami o dziale i autorze.
 */
public class ZgloszenieDTO {

    private Long id;
    private String tytul;
    private String opis;
    private ZgloszenieStatus status;
    private Long dzialId;
    private String dzialNazwa;
    private String autorUsername;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTytul() { return tytul; }
    public void setTytul(String tytul) { this.tytul = tytul; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public ZgloszenieStatus getStatus() { return status; }
    public void setStatus(ZgloszenieStatus status) { this.status = status; }

    public Long getDzialId() { return dzialId; }
    public void setDzialId(Long dzialId) { this.dzialId = dzialId; }

    public String getDzialNazwa() { return dzialNazwa; }
    public void setDzialNazwa(String dzialNazwa) { this.dzialNazwa = dzialNazwa; }

    public String getAutorUsername() { return autorUsername; }
    public void setAutorUsername(String autorUsername) { this.autorUsername = autorUsername; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}