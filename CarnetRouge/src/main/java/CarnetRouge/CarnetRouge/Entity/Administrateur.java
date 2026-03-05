package CarnetRouge.CarnetRouge.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@DiscriminatorValue("ADMIN")
public class Administrateur extends Utilisateurs {

    private String fonction;
}