package drimer.drimain.model;

import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.model.enums.ZgloszeniePriorytet;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Encja zgłoszenia.
 * Używa Bean Validation zamiast ręcznej metody validate().
 */
@Entity
public class Zgloszenie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Typ jest wymagany")
    private String typ;
    
    @NotBlank(message = "Imię jest wymagane")
    private String imie;
    
    @NotBlank(message = "Nazwisko jest wymagane")
    private String nazwisko;

    // Nowe pole tytul (opcjonalne)
    private String tytul;

    @Enumerated(EnumType.STRING)
    private ZgloszenieStatus status;   // Możesz domyślnie ustawić NOWE / OPEN jeśli enum to przewiduje

    @NotBlank(message = "Opis jest wymagany")
    @Size(min = 10, message = "Opis musi mieć co najmniej 10 znaków")
    @Column(length = 2000)
    private String opis;
    
    @NotNull(message = "Priorytet jest wymagany")
    @Enumerated(EnumType.STRING)
    @Column(name = "priorytet", nullable = false)
    private ZgloszeniePriorytet priorytet;

    @PastOrPresent(message = "Data i godzina nie może być w przyszłości")
    @Column(name = "data_godzina")
    private LocalDateTime dataGodzina;

    // Auditing fields
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dzial_id")
    private Dzial dzial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private User autor;

    // JPA lifecycle methods
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (dataGodzina == null) {
            dataGodzina = createdAt;
        }
        if (status == null) {
            status = ZgloszenieStatus.OPEN;
        }
        if (priorytet == null) {
            priorytet = ZgloszeniePriorytet.MEDIUM;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // --- Gettery / Settery ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getDataGodzina() {
        return dataGodzina;
    }

    public void setDataGodzina(LocalDateTime dataGodzina) {
        this.dataGodzina = dataGodzina;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
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

    public ZgloszeniePriorytet getPriorytet() {
        return priorytet;
    }

    public void setPriorytet(ZgloszeniePriorytet priorytet) {
        this.priorytet = priorytet;
    }
}