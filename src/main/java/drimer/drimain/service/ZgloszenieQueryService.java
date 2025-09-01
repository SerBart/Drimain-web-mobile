package drimer.drimain.service;

import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.model.enums.ZgloszeniePriorytet;
import drimer.drimain.repository.ZgloszenieRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ZgloszenieQueryService {

    @Autowired
    private ZgloszenieRepository zgloszenieRepository;

    public Page<Zgloszenie> findWithFilters(ZgloszenieStatus status, String typ, Long dzialId, 
                                           Long autorId, String q, ZgloszeniePriorytet priorytet,
                                           LocalDateTime dataOd, LocalDateTime dataDo, 
                                           Pageable pageable) {
        
        Specification<Zgloszenie> spec = buildSpecification(status, typ, dzialId, autorId, q, priorytet, dataOd, dataDo);
        return zgloszenieRepository.findAll(spec, pageable);
    }

    private Specification<Zgloszenie> buildSpecification(ZgloszenieStatus status, String typ, Long dzialId,
                                                        Long autorId, String q, ZgloszeniePriorytet priorytet,
                                                        LocalDateTime dataOd, LocalDateTime dataDo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by status
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // Filter by typ (case-insensitive LIKE)
            if (typ != null && !typ.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("typ")), "%" + typ.toLowerCase() + "%"));
            }

            // Filter by dzial
            if (dzialId != null) {
                predicates.add(cb.equal(root.get("dzial").get("id"), dzialId));
            }

            // Filter by autor
            if (autorId != null) {
                predicates.add(cb.equal(root.get("autor").get("id"), autorId));
            }

            // Filter by priority
            if (priorytet != null) {
                predicates.add(cb.equal(root.get("priorytet"), priorytet));
            }

            // Filter by date range (dataGodzina field)
            if (dataOd != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataGodzina"), dataOd));
            }
            if (dataDo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataGodzina"), dataDo));
            }

            // Full-text search in multiple fields
            if (q != null && !q.trim().isEmpty()) {
                String searchTerm = "%" + q.toLowerCase() + "%";
                Predicate opisLike = cb.like(cb.lower(root.get("opis")), searchTerm);
                Predicate imieLike = cb.like(cb.lower(root.get("imie")), searchTerm);
                Predicate nazwiskoLike = cb.like(cb.lower(root.get("nazwisko")), searchTerm);
                Predicate tytulLike = cb.like(cb.lower(root.get("tytul")), searchTerm);
                
                predicates.add(cb.or(opisLike, imieLike, nazwiskoLike, tytulLike));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}