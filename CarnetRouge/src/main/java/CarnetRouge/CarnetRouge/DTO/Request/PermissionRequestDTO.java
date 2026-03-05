package CarnetRouge.CarnetRouge.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequestDTO {
    @NotBlank(message = "")
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private boolean active;
}
