package drimer.drimain.repository;

import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZgloszenieRepository extends JpaRepository<Zgloszenie, Long> {
    
    Page<Zgloszenie> findByDzial_Id(Long dzialId, Pageable pageable);
    
    Page<Zgloszenie> findByDzial_IdAndStatus(Long dzialId, ZgloszenieStatus status, Pageable pageable);
    
    Page<Zgloszenie> findByStatus(ZgloszenieStatus status, Pageable pageable);
}