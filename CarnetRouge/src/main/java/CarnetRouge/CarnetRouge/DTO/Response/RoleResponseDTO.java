package CarnetRouge.CarnetRouge.DTO.Response;

import jakarta.persistence.Column;

import java.util.Set;

public class RoleResponseDTO {
    private String name;
    private String description;
    private  Boolean active;
    private Set<PermissionResponseDTO> permissions;
}
