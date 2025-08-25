package drimer.drimain.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Dzial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nazwa;

    @OneToMany(mappedBy = "dzial")
    private List<Maszyna> maszyny;

    // Gettery i settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNazwa() { return nazwa; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }

    public List<Maszyna> getMaszyny() { return maszyny; }
    public void setMaszyny(List<Maszyna> maszyny) { this.maszyny = maszyny; }
}
