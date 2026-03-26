package CarnetRouge.CarnetRouge.GDU.Entity;

import CarnetRouge.CarnetRouge.GDET.Entity.Classes;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("AST")
public class Assistant extends Utilisateurs{
    private String fonction;
    @ManyToMany
    @JoinTable(
            name = "assistant_classes",
            joinColumns = @JoinColumn(name = "assistant_id"),
            inverseJoinColumns = @JoinColumn(name = "classe_id")
    )
    private Collection<Classes> classes = new ArrayList<>();
}
