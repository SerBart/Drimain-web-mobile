package drimer.drimain.model;

import drimer.drimain.model.enums.ZgloszenieStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Encja zgłoszenia.
 * Dodano metodę validate() używaną ręcznie w kontrolerze.
 * (Jeśli przejdziesz na Bean Validation, możesz usunąć validate() i dodać adnotacje @NotBlank itd.)
 */
@Entity
public class Zgloszenie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Przykładowe pola – możesz dodać adnotacje Bean Validation jeśli chcesz
    private String typ;
    private String imie;
    private String nazwisko;

    @Enumerated(EnumType.STRING)
    private ZgloszenieStatus status;   // Możesz domyślnie ustawić NOWE / OPEN jeśli enum to przewiduje

    @Column(length = 2000)
    private String opis;

    @Column(name = "data_godzina")
    private LocalDateTime dataGodzina;

    // Audit fields (A2: Add createdAt & updatedAt)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at") 
    private LocalDateTime updatedAt;

    // Department & Author Association (Task 2)
    @ManyToOne
    @JoinColumn(name = "dzial_id")
    private Dzial dzial;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private User autor;

    
    // --- JPA Lifecycle callbacks for audit fields ---
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        // Keep dataGodzina for backward compatibility, populate automatically
        if (this.dataGodzina == null) {
            this.dataGodzina = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    /**
     * Ręczna walidacja – wywoływana w kontrolerze.
     * Rzuca IllegalArgumentException jeśli coś jest niepoprawne.
     * Dostosuj reguły do realnych wymagań.
     */
    public void validate() {
        if (imie == null || imie.isBlank()) {
            throw new IllegalArgumentException("Imię jest wymagane");
        }
        if (nazwisko == null || nazwisko.isBlank()) {
            throw new IllegalArgumentException("Nazwisko jest wymagane");
        }
        if (typ == null || typ.isBlank()) {
            throw new IllegalArgumentException("Typ jest wymagany");
        }
        if (opis == null || opis.isBlank()) {
            throw new IllegalArgumentException("Opis jest wymagany");
        }
        if (opis.length() < 10) {
            throw new IllegalArgumentException("Opis musi mieć co najmniej 10 znaków");
        }
        if (dataGodzina == null) {
            throw new IllegalArgumentException("Data i godzina są wymagane");
        }
        // (opcjonalnie) zakaz dat z przyszłości / przeszłości:
        // if (dataGodzina.isAfter(LocalDateTime.now().plusMinutes(5))) {
        //     throw new IllegalArgumentException("Data/godzina nie może być w odległej przyszłości");
        // }
    }
}