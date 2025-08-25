package drimer.drimain.repository;

import drimer.drimain.model.Osoba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OsobaRepository extends JpaRepository<Osoba, Long> {

    // Nowa metoda: znajdź po loginie
    Optional<Osoba> findByLogin(String login);


    // ... inne metody, jeśli masz (np. List<Osoba> findByDzial(Dzial dzial);)
}
