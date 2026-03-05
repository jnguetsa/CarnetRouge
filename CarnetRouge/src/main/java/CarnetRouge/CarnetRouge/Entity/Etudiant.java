package CarnetRouge.CarnetRouge.Entity;

import CarnetRouge.CarnetRouge.Enum.TypeNiveau;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("ETU") // ✅ discriminator pour SINGLE_TABLE
public class Etudiant extends Utilisateurs {

    private String matricule; // ✅ minuscule (convention Java)

    @Enumerated(EnumType.STRING) // ✅ stocke le nom de l'enum en BDD
    private TypeNiveau niveau; // ✅ minuscule
}