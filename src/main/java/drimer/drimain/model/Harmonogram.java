package drimer.drimain.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "harmonogramy")
public class Harmonogram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private String opis;

    @ManyToOne
    @JoinColumn(name = "maszyna_id")
    private Maszyna maszyna;

    @ManyToOne
    @JoinColumn(name = "osoba_id")
    private Osoba osoba;

    private String status; // np. "planowane", "w trakcie", "zakończone"

    // Gettery i settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
    public Maszyna getMaszyna() { return maszyna; }
    public void setMaszyna(Maszyna maszyna) { this.maszyna = maszyna; }
    public Osoba getOsoba() { return osoba; }
    public void setOsoba(Osoba osoba) { this.osoba = osoba; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}