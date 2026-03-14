package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActiveRoleDTORequest {
    private  Long id;
    private Boolean active;
}
