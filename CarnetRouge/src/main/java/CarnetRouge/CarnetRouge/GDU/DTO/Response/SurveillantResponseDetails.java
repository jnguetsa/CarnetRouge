package CarnetRouge.CarnetRouge.GDU.DTO.Response;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter
public class SurveillantResponseDetails {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;
    private boolean active;
    private boolean firstLogin;
    private String secteur;
    private String typeContrat;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<RoleResponseDTO> roles;
}
