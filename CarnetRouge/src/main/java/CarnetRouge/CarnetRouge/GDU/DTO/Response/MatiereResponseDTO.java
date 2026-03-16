package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatiereResponseDTO {
    private  Long id;
    private String nom;
    private String code;
    private  String libelle;
}
