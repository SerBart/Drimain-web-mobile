package drimer.drimain.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Maszyna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nazwa;

    @OneToMany(mappedBy = "maszyna")
    private List<Raport> raporty;

    @ManyToOne
    @JoinColumn(name = "dzial_id")
    private Dzial dzial;

    // getter i setter


    // Gettery i settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNazwa() { return nazwa; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }
    public Dzial getDzial() { return dzial; }
    public void setDzial(Dzial dzial) { this.dzial = dzial; }
    public List<Raport> getRaporty() { return raporty; }
    public void setRaporty(List<Raport> raporty) { this.raporty = raporty; }
}
