package drimer.drimain.controller;

import drimer.drimain.api.dto.*;
import drimer.drimain.api.mapper.ZgloszenieMapper;
import drimer.drimain.model.Zgloszenie; // TODO
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.repository.ZgloszenieRepository; // TODO
import drimer.drimain.util.ZgloszenieStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/zgloszenia")
@RequiredArgsConstructor
public class ZgloszenieRestController {

    private final ZgloszenieRepository zgloszenieRepository;

    @GetMapping
    public List<ZgloszenieDTO> list(@RequestParam Optional<String> status,
                                    @RequestParam Optional<String> typ,
                                    @RequestParam Optional<String> q) {

        return zgloszenieRepository.findAll().stream()
                .filter(z -> status
                        .map(s -> {
                            ZgloszenieStatus ms = ZgloszenieStatusMapper.map(s);
                            return ms != null && ms == z.getStatus();
                        })
                        .orElse(true))
                .filter(z -> typ
                        .map(t -> z.getTyp() != null && z.getTyp().equalsIgnoreCase(t))
                        .orElse(true))
                .filter(z -> q
                        .map(query -> {
                            String qq = query.toLowerCase();
                            return (z.getOpis() != null && z.getOpis().toLowerCase().contains(qq)) ||
                                    (z.getTyp() != null && z.getTyp().toLowerCase().contains(qq)) ||
                                    (z.getImie() != null && z.getImie().toLowerCase().contains(qq)) ||
                                    (z.getNazwisko() != null && z.getNazwisko().toLowerCase().contains(qq));
                        })
                        .orElse(true))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ZgloszenieDTO get(@PathVariable Long id) {
        Zgloszenie z = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found"));
        return toDto(z);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZgloszenieDTO create(@RequestBody ZgloszenieCreateRequest req) {
        Zgloszenie z = new Zgloszenie();
        ZgloszenieStatus mapped = ZgloszenieStatusMapper.map(req.getStatus());
        z.setStatus(mapped != null ? mapped : ZgloszenieStatus.NEW);
        z.setTyp(req.getTyp());
        z.setImie(req.getImie());
        z.setNazwisko(req.getNazwisko());
        z.setOpis(req.getOpis());
        z.setDataGodzina(req.getDataGodzina() != null ? req.getDataGodzina() : LocalDateTime.now());
        // TODO: obsługa zdjęcia
        zgloszenieRepository.save(z);
        return toDto(z);
    }

    @PutMapping("/{id}")
    public ZgloszenieDTO update(@PathVariable Long id, @RequestBody ZgloszenieUpdateRequest req) {
        Zgloszenie z = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found"));
        if (req.getTyp() != null) z.setTyp(req.getTyp());
        if (req.getImie() != null) z.setImie(req.getImie());
        if (req.getNazwisko() != null) z.setNazwisko(req.getNazwisko());
        if (req.getOpis() != null) z.setOpis(req.getOpis());
        if (req.getStatus() != null) {
            ZgloszenieStatus ms = ZgloszenieStatusMapper.map(req.getStatus());
            if (ms != null) z.setStatus(ms);
        }
        if (req.getDataGodzina() != null) {
            z.setDataGodzina(req.getDataGodzina());
        }
        zgloszenieRepository.save(z);
        return toDto(z);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        zgloszenieRepository.deleteById(id);
    }

    private ZgloszenieDTO toDto(Zgloszenie z) {
        return ZgloszenieMapper.toDto(z);
    }

    // Proste klasy request (możesz dać do osobnego pakietu)
    public static class ZgloszenieCreateRequest {
        private String typ;
        private String imie;
        private String nazwisko;
        private String status; // tekstowy wariant
        private String opis;
        private LocalDateTime dataGodzina;

        public String getTyp() { return typ; }
        public void setTyp(String typ) { this.typ = typ; }
        public String getImie() { return imie; }
        public void setImie(String imie) { this.imie = imie; }
        public String getNazwisko() { return nazwisko; }
        public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getOpis() { return opis; }
        public void setOpis(String opis) { this.opis = opis; }
        public LocalDateTime getDataGodzina() { return dataGodzina; }
        public void setDataGodzina(LocalDateTime dataGodzina) { this.dataGodzina = dataGodzina; }
    }

    public static class ZgloszenieUpdateRequest {
        private String typ;
        private String imie;
        private String nazwisko;
        private String status;
        private String opis;
        private LocalDateTime dataGodzina;

        public String getTyp() { return typ; }
        public void setTyp(String typ) { this.typ = typ; }
        public String getImie() { return imie; }
        public void setImie(String imie) { this.imie = imie; }
        public String getNazwisko() { return nazwisko; }
        public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getOpis() { return opis; }
        public void setOpis(String opis) { this.opis = opis; }
        public LocalDateTime getDataGodzina() { return dataGodzina; }
        public void setDataGodzina(LocalDateTime dataGodzina) { this.dataGodzina = dataGodzina; }
    }
}