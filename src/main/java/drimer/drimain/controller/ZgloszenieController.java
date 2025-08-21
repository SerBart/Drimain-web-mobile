package drimer.drimain.controller;
import drimer.drimain.model.Raport;
import org.springframework.beans.factory.annotation.Autowired;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.repository.*;
import drimer.drimain.service.RaportService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
@Controller
public class ZgloszenieController {

    @Autowired
    private RaportService raportService; // Teraz powinno działać
    @Autowired
    private MaszynaRepository maszynaRepository;

    @Autowired
    private OsobaRepository osobaRepository;

    @Autowired
    private DzialRepository dzialRepository;

    @Autowired
    private RaportRepository raportRepository;

    @Autowired
    private ZgloszenieRepository zgloszenieRepository;

    // Przykład w kontrolerze (dostosuj do swojej metody, np. /zgloszenie/zapisz)
    @PostMapping("/zgloszenie/zapisz")
    public String zapiszZgloszenie(@RequestParam(required = false) Long id,
                                   @RequestParam String imie,
                                   @RequestParam String nazwisko,
                                   @RequestParam String typ,
                                   @RequestParam String dataGodzina,  // String z formularza
                                   @RequestParam String opis,
                                   Model model) {  // Dodaj Model dla błędów
        try {
            Zgloszenie zgloszenie = id != null ? zgloszenieRepository.findById(id).orElse(new Zgloszenie()) : new Zgloszenie();

            zgloszenie.setImie(imie);
            zgloszenie.setNazwisko(nazwisko);
            zgloszenie.setTyp(typ);
            zgloszenie.setDataGodzina(LocalDateTime.parse(dataGodzina));  // Parsowanie – tu był Twój błąd!
            zgloszenie.setOpis(opis);

            // Opcjonalnie: Walidacja
            zgloszenie.validate();

            // Zapisz do bazy (zakładam, że masz repozytorium)
            zgloszenieRepository.save(zgloszenie);

            return "redirect:/zgloszenia";  // Sukces – przekieruj na listę
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Błąd parsowania daty/godziny: " + e.getMessage() + ". Poprawny format: yyyy-MM-ddTHH:mm (np. 2025-07-25T12:00)");
            // Dodaj atrybuty modelu, by wrócić do formularza
            model.addAttribute("maszyny", maszynaRepository.findAll());
            model.addAttribute("osoby", osobaRepository.findAll());
            return "zgloszenia";  // Wróć z błędem (lub do formularza)
        } catch (Exception e) {
            model.addAttribute("error", "Nieoczekiwany błąd: " + e.getMessage());
            return "zgloszenia";
        }
    }


    @GetMapping("/zgloszenia")
    public String zgloszenia(Model model) {
        // Usunięto sprawdzanie sesji i isLoggedIn – zawsze dostępny
        boolean isAdmin = true; // Domyślnie admin, skoro bez logowania
        List<Zgloszenie> zgloszenia = zgloszenieRepository.findAll(); // Zawsze pokazuje wszystkie

        model.addAttribute("isLoggedIn", true);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("zgloszenia", zgloszenia);
        return "zgloszenia";
    }
//    @GetMapping("/zgloszenie/nowy")
//    public String nowyForm(Model model) {
//        // dodaj potrzebne atrybuty
//        model.addAttribute("maszyny", maszynaRepository.findAll());
//        model.addAttribute("osoby", osobaRepository.findAll());
//        return "zgloszenie-form :: addForm"; // zwracamy fragment "addForm"
//    }
    @GetMapping("/zgloszenia/nowy")
    public String nowyRaportForm(Model model, @RequestParam(value = "fragment", required = false) boolean fragment) {
        model.addAttribute("raport", new Zgloszenie());  // Nowy pusty raport
        model.addAttribute("maszyny", maszynaRepository.findAll());
        model.addAttribute("osoby", osobaRepository.findAll());     // Lista osób

        System.out.println("Maszyny: " + maszynaRepository.findAll().size());  // Wypisze w konsoli serwera
        if (fragment) {
            return "zgloszenie-form :: addForm";  // Upewnij się, że plik to raport-form.html i fragment "addForm"
        }
        return "zgloszenie-form";  // Jeśli nie fragment, pełna strona (dla testu)
    }



    @PostMapping("/zgloszenia/add")
    public String addZgloszenie(@RequestParam String imie,
                                @RequestParam String nazwisko,
                                @RequestParam String typ,
                                @RequestParam String dataGodzina,
                                @RequestParam String opis) {
        // Usunięto sprawdzanie sesji
        Zgloszenie zgloszenie = new Zgloszenie();
        zgloszenie.setImie(imie);
        zgloszenie.setNazwisko(nazwisko);
        zgloszenie.setTyp(typ);
        zgloszenie.setDataGodzina(LocalDateTime.parse(dataGodzina));
        zgloszenie.setOpis(opis);

        zgloszenieRepository.save(zgloszenie);
        return "redirect:/zgloszenia";
    }
    @GetMapping("/zgloszenie/edytuj/{id}")
    public String edytujZgloszenieForm(@PathVariable Long id, Model model, @RequestParam(value = "fragment", required = false) boolean fragment) {
        Zgloszenie zgloszenie = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgłoszenie o ID " + id + " nie istnieje"));  // Dodaj to, by rzucać błędem jeśli nie znaleziono
        model.addAttribute("zgloszenie", zgloszenie);
        model.addAttribute("maszyny", maszynaRepository.findAll());
        model.addAttribute("osoby", osobaRepository.findAll());
        // Debug: Wypisz w konsoli serwera
        System.out.println("Edycja zgłoszenia ID: " + id + ", Maszyny: " + maszynaRepository.findAll().size());
        if (fragment) {
            return "zgloszenie-form :: editForm";  // Upewnij się, że plik to zgloszenie-form.html i fragment "editForm"
        }
        return "zgloszenie_form";  // Dla testu pełna strona
    }


    // Zapisz edycję (POST)
    @PostMapping("/zgloszenia/edit")
    public String saveEditedZgloszenie(@RequestParam Long id,
                                       @RequestParam String imie,
                                       @RequestParam String nazwisko,
                                       @RequestParam String typ,
                                       @RequestParam String dataGodzina,
                                       @RequestParam String opis) {
        Zgloszenie zgloszenie = zgloszenieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        zgloszenie.setImie(imie);
        zgloszenie.setNazwisko(nazwisko);
        zgloszenie.setTyp(typ);
        zgloszenie.setDataGodzina(LocalDateTime.parse(dataGodzina));
        zgloszenie.setOpis(opis);
        zgloszenieRepository.save(zgloszenie);
        return "redirect:/zgloszenia";
    }

    // Usunięcie zgłoszenia (POST)
    @PostMapping("/zgloszenia/delete")
    public String deleteZgloszenie(@RequestParam Long id) {
        zgloszenieRepository.deleteById(id);
        return "redirect:/zgloszenia";
    }
}
