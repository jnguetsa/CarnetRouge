package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDTO {
    private Long id;
    private String name;
    private String description;
    private  Boolean active;
    private LocalDateTime creatAt;
    private Set<PermissionResponseDTO> permissions;
}
