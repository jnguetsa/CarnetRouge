package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import CarnetRouge.CarnetRouge.GDU.DTO.Response.RoleResponseDTO;
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
public class AssistantRequestDTO {
    private Long id;
    private String fonction;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Date dateNaissance;
    private Set<RoleResponseDTO> roles;
}
