package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
    @ManyToMany(mappedBy = "enseignants")
    private Collection<UE> ues = new ArrayList<>();
}