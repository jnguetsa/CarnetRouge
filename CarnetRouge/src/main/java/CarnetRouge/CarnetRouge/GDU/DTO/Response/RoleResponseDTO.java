package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import java.util.Set;

public class RoleResponseDTO {
    private String name;
    private String description;
    private  Boolean active;
    private Set<PermissionResponseDTO> permissions;
}
