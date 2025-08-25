package drimer.drimain.service;


import drimer.drimain.model.Raport; // Zakładam, że entity Raport jest w pakiecie model
import drimer.drimain.repository.RaportRepository; // Import repozytorium
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaportService {

    @Autowired
    private RaportRepository raportRepository; // Wstrzykiwanie repozytorium

    // Metoda do pobierania wszystkich raportów (używana w liście)
    public List<Raport> pobierzWszystkie() {
        return raportRepository.findAll();
    }

    // Metoda do usuwania raportu po ID
    public void usun(Long id) {
        raportRepository.deleteById(id); // Usuwanie z bazy
    }

    // Inne metody, np. dodawanie, edycja – dodaj wg potrzeb
}
