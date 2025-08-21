package drimer.drimain.api.controller;

import drimer.drimain.api.dto.*;
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
                .filter(z -> status.map(s -> s.equalsIgnoreCase(z.getStatus())).orElse(true))
                .filter(z -> typ.map(t -> t.equalsIgnoreCase(z.getTyp())).orElse(true))
                .filter(z -> q.map(query ->
                        (z.getOpis() != null && z.getOpis().toLowerCase().contains(query.toLowerCase())) ||
                        (z.getTyp() != null && z.getTyp().toLowerCase().contains(query.toLowerCase()))
                ).orElse(true))
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
        z.setStatus(mapped != null ? mapped : ZgloszenieStatus.OPEN);
        z.setTyp(req.getTyp());
        z.setImie(req.getImie());
        z.setNazwisko(req.getNazwisko());
        z.setStatus(req.getStatus());
        z.setOpis(req.getOpis());
        z.setDataGodzina(LocalDateTime.now());
        // TODO: photo handling
        zgloszenieRepository.save(z);
        return toDto(z);
    }

    @PutMapping("/{id}")
    public ZgloszenieDTO update(@PathVariable Long id, @RequestBody ZgloszenieUpdateRequest req) {
        Zgloszenie z = zgloszenieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zgloszenie not found"));
        if (req.getTyp() != null) z.setTyp(req.getTyp());
        if (req.getStatus() != null) z.setStatus(req.getStatus());
        if (req.getOpis() != null) z.setOpis(req.getOpis());
        if (req.getStatus() != null) {
            ZgloszenieStatus ms = ZgloszenieStatusMapper.map(req.getStatus());
            if (ms != null) z.setStatus(ms);
        }
        // TODO: photo update
        zgloszenieRepository.save(z);
        return toDto(z);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        zgloszenieRepository.deleteById(id);
    }

    private ZgloszenieDTO toDto(Zgloszenie z) {
        ZgloszenieDTO dto = new ZgloszenieDTO();
        dto.setId(z.getId());
        dto.setDataGodzina(z.getDataGodzina());
        dto.setTyp(z.getTyp());
        dto.setImie(z.getImie());
        dto.setNazwisko(z.getNazwisko());
        dto.setStatus(z.getStatus());
        dto.setOpis(z.getOpis());
        dto.setHasPhoto(false);
        return dto;
    }
}