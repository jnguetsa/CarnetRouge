package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlageHoraire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate jour; // Jour de la semaine (ex: lundi, mardi)
    private LocalTime heureDebut;
    private LocalTime heureFin;

    // Relations bidirectionnelles
    @OneToMany(mappedBy = "plageHoraire")
    private Collection<Evenement> evenements = new ArrayList<>();

    @ManyToMany(mappedBy = "plagesHoraires")
    private Collection<UE> ues = new ArrayList<>();
}

