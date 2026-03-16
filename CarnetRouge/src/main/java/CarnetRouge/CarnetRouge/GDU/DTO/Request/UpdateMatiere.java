package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMatiere {
    private  Long id;
    private String nom;
    private String code;
    private  String libelle;
}
