package drimer.drimain.model; // Zmień na Twój package, jeśli inny

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "osoby") // Zakładam nazwę tabeli z Twojej bazy
public class Osoba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String haslo;
    private String imieNazwisko; // Zakładam to pole z Twojego oryginalnego kodu
    private String rola; // Pole roli, jak dodaliśmy wcześniej ("ADMIN" lub "USER")

    // Domyślny konstruktor (wymagany dla JPA)
    public Osoba() {}

    // Konstruktor z parametrami (użyteczny w DataInitializer)
    public Osoba(String login, String haslo, String rola) {
        this.login = login;
        this.haslo = haslo;
        this.rola = rola;
    }

    // Gettery i settery dla wszystkich pól
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {  // Brakujący getter!
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getImieNazwisko() {
        return imieNazwisko;
    }

    public void setImieNazwisko(String imieNazwisko) {
        this.imieNazwisko = imieNazwisko;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola = rola;
    }
}
