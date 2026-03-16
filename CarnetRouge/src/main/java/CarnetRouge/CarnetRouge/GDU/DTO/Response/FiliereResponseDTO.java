package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FiliereResponseDTO {
    private Long id;
    private String nom;
    private String code;
    private String description;
}
