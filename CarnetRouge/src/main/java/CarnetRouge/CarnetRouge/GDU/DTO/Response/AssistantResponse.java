package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssistantResponse {
    private String fonction;
    private String nom;
    private String prenom;
    private String email;
    private Date dateNaissance;
    private Set<PermissionResponseDTO> permissions;

}
