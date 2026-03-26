package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssistantResponseDetails {
    private Long id;
    private String fonction;
    private String nom;
    private String prenom;
    private String email;
    private Date dateNaissance;
    protected  String telephone;
    protected boolean active ;
    protected boolean firstLogin;
    protected LocalDateTime createdAt;
    private List<ClassesResponseDTO> classes;
    private  Set<RoleResponseDTO> roles;
}
