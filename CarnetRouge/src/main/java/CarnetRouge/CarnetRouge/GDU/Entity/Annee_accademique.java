package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Annee_accademique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 8, nullable = false)
    private String nom;
    private boolean active;
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;

}
