package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private Boolean active;

    @Column(updatable = false, nullable = false)
    protected LocalDateTime creatAt;

    @LastModifiedDate
    @Column(insertable = false)
    protected LocalDateTime updateAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "utilisateur_role",  // Nom exact de la table de jointure
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "utilisateur_id")
    )
    private Set<Utilisateurs> utilisateurs = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "role_permission",  // Si cette table existe bien
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
}
