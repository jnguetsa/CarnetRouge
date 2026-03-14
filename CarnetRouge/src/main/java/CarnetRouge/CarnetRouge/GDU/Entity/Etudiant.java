package CarnetRouge.CarnetRouge.GDU.Entity;

import CarnetRouge.CarnetRouge.GDU.Enum.TypeNiveau;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("ETU")
public class Etudiant extends Utilisateurs {
    @Column(unique = true, nullable = false)
    private String matricule;
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeNiveau niveau;
}