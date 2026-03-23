package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // "Un cours apparaît dans plusieurs séances"
    @OneToMany(mappedBy = "ue")
    private Collection<PlageHoraire> plagesHoraires = new ArrayList<>();

    // "Un cours peut être enseigné par plusieurs enseignants"
    @ManyToMany
    @JoinTable(
            name = "ue_enseignant",
            joinColumns = @JoinColumn(name = "ue_id"),
            inverseJoinColumns = @JoinColumn(name = "enseignant_id")
    )
    @Builder.Default
    private Collection<Enseignant> enseignants = new ArrayList<>();

    // "Un cours est suivi par plusieurs classes"
    @ManyToMany(mappedBy = "ue")
    private Collection<Classes> classes = new ArrayList<>();
}