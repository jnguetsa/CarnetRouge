package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "plage_horaire_id")
    private PlageHoraire plageHoraire;
}

