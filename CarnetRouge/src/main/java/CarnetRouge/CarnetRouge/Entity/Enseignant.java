package CarnetRouge.CarnetRouge.Entity;

import CarnetRouge.CarnetRouge.Enum.STypeEnseignant;
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
@DiscriminatorValue("ENS")
public class Enseignant extends Utilisateurs {

    private String grade;
    //@Enumerated(EnumType.STRING)
    private String typeEnseignant;
}