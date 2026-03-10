package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateursDTO {


    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Date dateNaissance;
    private boolean active;
    private String type;          // "ENS" ou "ASS"
    private String grade;         // Enseignant seulement
    private String typeEnseignant;// Enseignant seulement
    private String fonction;      // Assistant seulement
    private Set<RoleResponseDTO> roles;
}
