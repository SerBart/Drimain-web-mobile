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

    private String tytul;

    @Column(length = 2000)
    private String opis;

    @Enumerated(EnumType.STRING)
    private ZgloszenieStatus status = ZgloszenieStatus.NEW;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dzial_id", nullable = false)
    private Dzial dzial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private User autor;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // TODO: Add timestamps management if needed

    // --- Gettery / Settery ---
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

    /**
     * Ręczna walidacja – wywoływana w kontrolerze.
     * Rzuca IllegalArgumentException jeśli coś jest niepoprawne.
     * Dostosuj reguły do realnych wymagań.
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