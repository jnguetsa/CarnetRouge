package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

@Getter @Setter
public class SurveillantResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;
    private boolean active;
    private String secteur;
    private String typeContrat;
    private Set<RoleResponseDTO> roles;
}
