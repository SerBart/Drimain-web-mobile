package drimer.drimain.controller;

import drimer.drimain.model.Raport;
import drimer.drimain.repository.*;
import drimer.drimain.service.RaportService; // Dodaj ten import!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class RaportController {

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


    // Endpoint do usuwania
    @GetMapping("/raport/usun/{id}")
    public String usunRaport(@PathVariable Long id) {
        raportService.usun(id); // Wywołanie serwisu
        return "redirect:/"; // Przekierowanie na listę po usunięciu
    }
    // Nowy: Lista wszystkich raportów (GET)
    @GetMapping("/raporty")
    public String raporty() {
        return "raporty"; // Tylko to – bez ifów na sesję/uprawnienia
    }

    // Istniejący: Nowy raport (GET) – dostosowany do modala (zwraca fragment jeśli param 'fragment')
    @GetMapping("/raport/nowy")
    public String nowyRaportForm(Model model, @RequestParam(value = "fragment", required = false) boolean fragment) {
        model.addAttribute("raport", new Raport());  // Nowy pusty raport
        model.addAttribute("maszyny", maszynaRepository.findAll());  // Lista maszyn
        model.addAttribute("osoby", osobaRepository.findAll());      // Lista osób
        // Dodaj debug: Sprawdź, czy listy nie są puste
        System.out.println("Maszyny: " + maszynaRepository.findAll().size());  // Wypisze w konsoli serwera
        if (fragment) {
            return "raport-form :: addForm";  // Upewnij się, że plik to raport-form.html i fragment "addForm"
        }
        return "raport-form";  // Jeśli nie fragment, pełna strona (dla testu)
    }


    // Istniejący: Edycja raportu (GET) – dostosowany do modala
    @GetMapping("/raport/edytuj/{id}")
    public String edytujRaportForm(@PathVariable Long id, Model model, @RequestParam(value = "fragment", required = false) boolean fragment) {
        Raport raport = raportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        model.addAttribute("raport", raport);
        model.addAttribute("dzialy", dzialRepository.findAll());
        model.addAttribute("maszyny", maszynaRepository.findAll());
        model.addAttribute("osoby", osobaRepository.findAll());
        if (fragment) {
            return "raporty :: editForm"; // Zwróć fragment dla modala
        }
        return "raport-form";
    }

    // Istniejący: Zapisz raport (POST) – bez zmian
    @PostMapping("/raport/zapisz")
    public String zapiszRaport(@RequestParam(required = false) Long id,
                               @RequestParam Long maszynaId,
                               @RequestParam String typNaprawy,
                               @RequestParam String opis,
                               @RequestParam Long osobaId,
                               @RequestParam String status,
                               @RequestParam String dataNaprawy,
                               @RequestParam String czasOd,
                               @RequestParam String czasDo) {
        Raport raport = id != null ? raportRepository.findById(id).orElse(new Raport()) : new Raport();
        raport.setMaszyna(maszynaRepository.findById(maszynaId).orElse(null));
        raport.setTypNaprawy(typNaprawy);
        raport.setOpis(opis);
        raport.setOsoba(osobaRepository.findById(osobaId).orElse(null));
        raport.setStatus(status);
        raport.setDataNaprawy(LocalDate.parse(dataNaprawy));
        raport.setCzasOd(LocalTime.parse(czasOd));
        raport.setCzasDo(LocalTime.parse(czasDo));
        raportRepository.save(raport);
        return "redirect:/raporty"; // Redirect do listy
    }

    // Nowy: Usunięcie raportu (POST)
    @PostMapping("/raport/delete")
    public String deleteRaport(@RequestParam Long id) {
        raportRepository.deleteById(id);
        return "redirect:/raporty";
    }


    // Inne endpointy, np. /raport/nowy, /raport/edytuj/{id}
}
