package drimer.drimain.model;

import drimer.drimain.model.enums.ZgloszenieStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Encja zgłoszenia - rozszerzona struktura zachowuje kompatybilność wstecz.
 * Stare pola (typ, imie, nazwisko, dataGodzina) + nowe pola (tytul, dzial, autor).
 * Dodano metodę validate() używaną ręcznie w kontrolerze.
 * (Jeśli przejdziesz na Bean Validation, możesz usunąć validate() i dodać adnotacje @NotBlank itd.)
 */
@Entity
@Table(name = "zgloszenia")
public class Zgloszenie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Stare pola dla kompatybilności wstecz ---
    private String typ;
    private String imie;
    private String nazwisko;

    @Column(name = "data_godzina")
    private LocalDateTime dataGodzina;

    // --- Nowe pola dla nowej funkcjonalności ---
    @Column(length = 200)
    private String tytul;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dzial_id")
    private Dzial dzial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private User autor;

    // --- Status wspólny dla obu schematów ---
    @Enumerated(EnumType.STRING)
    private ZgloszenieStatus status;

    @Column(length = 4000)
    private String opis;

    // --- Gettery / Settery dla starych pól ---
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

    public LocalDateTime getDataGodzina() {
        return dataGodzina;
    }

    public void setDataGodzina(LocalDateTime dataGodzina) {
        this.dataGodzina = dataGodzina;
    }

    // --- Gettery / Settery dla nowych pól ---
    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
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

    // --- Gettery / Settery wspólne ---
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

    /**
     * Ręczna walidacja – wywoływana w kontrolerze (stary format).
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