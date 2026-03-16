package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UERequestDTO {
    private Long id;
    private String nom;
    private String code;
    private String libelle;
}
