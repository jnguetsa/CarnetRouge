package CarnetRouge.CarnetRouge.DTO.Response;

import java.util.Date;
import java.util.Set;

public class EtudiantResponseDTO {

    private  Long id;
    private String nom;
    private String prenom;
    private String email;
    private Date dateNaissance;
    private Set<RoleResponseDTO> roles;
}
