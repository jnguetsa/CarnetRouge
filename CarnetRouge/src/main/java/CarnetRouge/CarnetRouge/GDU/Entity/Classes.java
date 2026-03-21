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
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Classes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime updatedAt;

    // "Une classe a plusieurs séances dans son EDT"
    @OneToMany(mappedBy = "classe")
    private Collection<PlageHoraire> plagesHoraires = new ArrayList<>();

    // "Une classe suit plusieurs cours"
    @ManyToMany
    @JoinTable(
            name = "classes_ue",
            joinColumns = @JoinColumn(name = "classes_id"),
            inverseJoinColumns = @JoinColumn(name = "ue_id")
    )
    private Collection<UE> ue = new ArrayList<>();

    // "Une classe appartient à une seule spécialité"
    @ManyToOne
    @JoinColumn(name = "specialite_id")
    private Specialite specialite;
}