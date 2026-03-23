package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Collection;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Specialite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private String code;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();
    @LastModifiedDate
    private LocalDateTime updateAt;
    @OneToMany
    private Collection<Classes> classes;
    @ManyToOne
    private Filiere filiere;

}
