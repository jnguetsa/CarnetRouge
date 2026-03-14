package CarnetRouge.CarnetRouge.GDU.DTO.Response;



import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class EnseignantResponseDetails {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Date dateNaissance;
    private String telephone;
    private boolean active;
    private boolean firstLogin;
    private LocalDateTime createdAt;

    // Champs spécifiques Enseignant
    private String grade;
    private String typeEnseignant;

    private Set<RoleResponseDTO> roles;
}
