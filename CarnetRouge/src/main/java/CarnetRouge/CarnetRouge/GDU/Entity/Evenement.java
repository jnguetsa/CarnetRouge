package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private String couleur;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // "Plusieurs événements appartiennent à une seule séance"
    @ManyToOne
    @JoinColumn(name = "plage_horaire_id")
    private PlageHoraire plageHoraire;
}

/*

        ---

        ## Récapitulatif des tables en base

plage_horaire        → colonnes : id, jour, heureDebut, heureFin,
salle, couleur, classe_id,
ue_id, enseignant_id

ue_enseignant        → colonnes : ue_id, enseignant_id
classes_ue           → colonnes : classes_id, ue_id
evenement            → colonnes : id, nom, description,
couleur, plage_horaire_id */


