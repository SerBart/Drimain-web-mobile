package drimer.drimain.repository;

import drimer.drimain.model.Zgloszenie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZgloszenieRepository extends JpaRepository<Zgloszenie, Long> {
}