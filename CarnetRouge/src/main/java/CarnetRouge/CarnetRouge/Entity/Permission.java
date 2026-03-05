package CarnetRouge.CarnetRouge.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @NotBlank(message = "Veillez renseigner la permission")
    private  String name;

    @NotBlank(message = "Veillez renseigner la description")
    @Column(unique = true, nullable = false)
    private  String description;
    private  Boolean active;
    @Column( updatable = false, nullable = false)
    protected LocalDateTime creatAt;
    @LastModifiedDate
    @Column( insertable = false)
    protected LocalDateTime updateAt;
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles=  new HashSet<>();
}
