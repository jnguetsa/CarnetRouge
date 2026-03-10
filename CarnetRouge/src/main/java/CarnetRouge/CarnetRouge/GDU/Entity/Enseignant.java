package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("ENS")
public class Enseignant extends Utilisateurs {

    private String grade;
    private String typeEnseignant;
}