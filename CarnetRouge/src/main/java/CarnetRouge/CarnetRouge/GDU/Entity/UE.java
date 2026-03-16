package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String code;
    private String libelle;
    private Long nbrCredit;
    private Long dheure;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @ManyToMany
    @JoinTable(
            name = "ue_plage_horaire",
            joinColumns = @JoinColumn(name = "ue_id"),
            inverseJoinColumns = @JoinColumn(name = "plage_horaire_id")
    )
    private Collection<PlageHoraire> plagesHoraires = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "ue_enseignant",
            joinColumns = @JoinColumn(name = "ue_id"),
            inverseJoinColumns = @JoinColumn(name = "enseignant_id")
    )
    private Collection<Enseignant> enseignants = new ArrayList<>();

    @ManyToMany(mappedBy = "ue")
    private Collection<Classes> classes = new ArrayList<>();
}

