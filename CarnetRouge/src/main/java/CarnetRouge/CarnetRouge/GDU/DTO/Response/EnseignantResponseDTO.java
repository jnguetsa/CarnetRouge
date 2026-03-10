package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter

public class EnseignantResponseDTO {
    private  Long id;
    private String nom;
    private String prenom;
    private String email;
    private Date dateNaissance;
    private String grade;
    private String typeEnseignant;
    private Set<RoleResponseDTO> roles;
}
