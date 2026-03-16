package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @ManyToMany
    @JoinTable(
            name = "classes_ue",
            joinColumns = @JoinColumn(name = "classes_id"),
            inverseJoinColumns = @JoinColumn(name = "ue_id")
    )
    private Collection<UE> ue = new ArrayList<>();

    @ManyToOne
    private Specialite specialite;
}

