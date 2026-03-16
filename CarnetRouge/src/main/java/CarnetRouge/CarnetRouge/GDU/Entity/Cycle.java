package CarnetRouge.CarnetRouge.GDU.Entity;

import CarnetRouge.CarnetRouge.GDU.Enum.TypeNiveau;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Collection;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Enumerated(EnumType.STRING)
    private TypeNiveau niveau;
    private String  nom;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "cycle")
    private Collection<Filiere> filieres;

}
