package CarnetRouge.CarnetRouge.DTO.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;

public class RoleRequestDTO {
    private Long id;
    @NotBlank(message = "Veuillez renseigner le rôle")
    @Column(unique = true, nullable = false)
    private String name;
    @NotBlank(message = "Veuillez renseigner la description")
    private String description;
    @NotBlank
    private  Boolean active;
    private Set<PermissionRequestDTO> permissions;
}
