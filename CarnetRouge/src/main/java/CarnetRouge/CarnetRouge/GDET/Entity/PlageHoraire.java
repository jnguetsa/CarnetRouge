package CarnetRouge.CarnetRouge.GDET.Entity;

import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import CarnetRouge.CarnetRouge.GDAE.Entity.UE;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PlageHoraire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate jour;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String salle;
    private String couleur;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // "Plusieurs séances appartiennent à une seule classe"
    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classes classe;

    // "Plusieurs séances concernent un seul cours"
    @ManyToOne
    @JoinColumn(name = "ue_id")
    private UE ue;

    // "Plusieurs séances sont assurées par un seul enseignant"
    @ManyToOne
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    // "Une séance peut avoir plusieurs événements"
    @OneToMany(mappedBy = "plageHoraire", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Evenement> evenements = new ArrayList<>();
}