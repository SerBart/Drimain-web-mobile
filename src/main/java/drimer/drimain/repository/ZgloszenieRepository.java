package drimer.drimain.repository;

import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ZgloszenieRepository extends JpaRepository<Zgloszenie, Long> {
    
    Page<Zgloszenie> findByDzialId(Long dzialId, Pageable pageable);
    
    Page<Zgloszenie> findByDzialIdAndStatus(Long dzialId, ZgloszenieStatus status, Pageable pageable);
    
    @Query("SELECT z FROM Zgloszenie z WHERE (:status IS NULL OR z.status = :status)")
    Page<Zgloszenie> findByStatusOrAll(@Param("status") ZgloszenieStatus status, Pageable pageable);
}