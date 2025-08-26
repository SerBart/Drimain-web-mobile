package drimer.drimain.repository;

import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ZgloszenieRepository extends JpaRepository<Zgloszenie, Long> {
    
    // Find by department for security scoping
    Page<Zgloszenie> findByDzialId(Long dzialId, Pageable pageable);
    
    // Find with status filter
    Page<Zgloszenie> findByStatus(ZgloszenieStatus status, Pageable pageable);
    
    // Find by department and status
    Page<Zgloszenie> findByDzialIdAndStatus(Long dzialId, ZgloszenieStatus status, Pageable pageable);
    
    // Advanced search with optional filters
    @Query("SELECT z FROM Zgloszenie z WHERE " +
           "(:status IS NULL OR z.status = :status) AND " +
           "(:dzialId IS NULL OR z.dzial.id = :dzialId) AND " +
           "(:q IS NULL OR LOWER(z.opis) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           " LOWER(z.typ) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           " LOWER(z.imie) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           " LOWER(z.nazwisko) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Zgloszenie> findWithFilters(@Param("status") ZgloszenieStatus status, 
                                    @Param("dzialId") Long dzialId,
                                    @Param("q") String q, 
                                    Pageable pageable);
}