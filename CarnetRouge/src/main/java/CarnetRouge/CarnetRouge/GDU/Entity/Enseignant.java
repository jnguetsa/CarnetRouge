package CarnetRouge.CarnetRouge.GDU.Entity;

import CarnetRouge.CarnetRouge.GDAE.Entity.UE;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;


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
    @ManyToMany(mappedBy = "enseignants", fetch = FetchType.LAZY)
    private Collection<UE> ues = new ArrayList<>();

}