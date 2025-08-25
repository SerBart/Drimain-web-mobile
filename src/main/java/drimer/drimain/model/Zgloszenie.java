package drimer.drimain.model;

import jakarta.persistence.*;
import lombok.Data;  // Lombok: Automatyczne gettery/settery – zgasi czerwone!
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;  // Zmienione na LocalDateTime dla dataGodzina

@Entity
@Table(name = "zgloszenie")
@Data  // To generuje wszystkie settery (setImie, setNazwisko itd.)
@NoArgsConstructor
@AllArgsConstructor
public class Zgloszenie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imie")
    private String imie;

    @Column(name = "nazwisko")
    private String nazwisko;

    @Column(name = "typ")
    private String typ;

    @Column(name = "data_godzina")
    private LocalDateTime dataGodzina;  // Zmienione na LocalDateTime dla pełnej daty + czasu

    @Column(name = "opis")
    private String opis;

    // Inne pola z Twojego repo (np. relacje) – dodaj jeśli potrzeba
    @ManyToOne
    @JoinColumn(name = "maszyna_id")
    private Maszyna maszyna;

    @ManyToOne
    @JoinColumn(name = "osoba_id")
    private Osoba osoba;

    @Column(name = "status")
    private String status;

    // Opcjonalna walidacja (wywołasz w kontrolerze)
    public void validate() {
        if (dataGodzina == null) {
            throw new IllegalArgumentException("Data i godzina muszą być podane");
        }
    }
}
