package drimer.drimain.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Raport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "maszyna_id")
    private Maszyna maszyna;

    private String typNaprawy;

    @Column(name = "opis", length = 1000)  // Zwiększamy limit do 1000 znaków (możesz zmienić na więcej, np. 5000)
    private String opis;

    @ManyToOne
    @JoinColumn(name = "osoba_id")
    private Osoba osoba;

    private String status;
    private LocalDate dataNaprawy;
    private LocalTime czasOd;
    private LocalTime czasDo;

    // Gettery i settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Maszyna getMaszyna() { return maszyna; }
    public void setMaszyna(Maszyna maszyna) { this.maszyna = maszyna; }
    public String getTypNaprawy() { return typNaprawy; }
    public void setTypNaprawy(String typNaprawy) { this.typNaprawy = typNaprawy; }
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
    public Osoba getOsoba() { return osoba; }
    public void setOsoba(Osoba osoba) { this.osoba = osoba; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDataNaprawy() { return dataNaprawy; }
    public void setDataNaprawy(LocalDate dataNaprawy) { this.dataNaprawy = dataNaprawy; }
    public LocalTime getCzasOd() { return czasOd; }
    public void setCzasOd(LocalTime czasOd) { this.czasOd = czasOd; }
    public LocalTime getCzasDo() { return czasDo; }
    public void setCzasDo(LocalTime czasDo) { this.czasDo = czasDo; }
}
