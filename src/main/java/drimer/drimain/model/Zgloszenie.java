package drimer.drimain.model;

import drimer.drimain.model.enums.ZgloszenieStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Encja zgłoszenia.
 * Updated to include department-based access control and author tracking.
 */
@Entity
public class Zgloszenie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tytul;

    @Enumerated(EnumType.STRING)
    private ZgloszenieStatus status = ZgloszenieStatus.NEW;

    @Column(length = 2000, nullable = false)
    private String opis;

    @ManyToOne
    @JoinColumn(name = "dzial_id", nullable = false)
    private Dzial dzial;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private User autor;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Legacy fields - kept for backward compatibility but may be deprecated
    private String typ;
    private String imie;
    private String nazwisko;

    @Column(name = "data_godzina")
    private LocalDateTime dataGodzina;

    // --- Getters / Setters ---
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

    public ZgloszenieStatus getStatus() {
        return status;
    }

    public void setStatus(ZgloszenieStatus status) {
        this.status = status;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Dzial getDzial() {
        return dzial;
    }

    public void setDzial(Dzial dzial) {
        this.dzial = dzial;
    }

    public User getAutor() {
        return autor;
    }

    public void setAutor(User autor) {
        this.autor = autor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Legacy fields getters/setters
    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public LocalDateTime getDataGodzina() {
        return dataGodzina;
    }

    public void setDataGodzina(LocalDateTime dataGodzina) {
        this.dataGodzina = dataGodzina;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Basic validation for required fields.
     */
    public void validate() {
        if (tytul == null || tytul.isBlank()) {
            throw new IllegalArgumentException("Tytuł jest wymagany");
        }
        if (opis == null || opis.isBlank()) {
            throw new IllegalArgumentException("Opis jest wymagany");
        }
        if (opis.length() < 10) {
            throw new IllegalArgumentException("Opis musi mieć co najmniej 10 znaków");
        }
        if (dzial == null) {
            throw new IllegalArgumentException("Dział jest wymagany");
        }
        if (autor == null) {
            throw new IllegalArgumentException("Autor jest wymagany");
        }
    }
}