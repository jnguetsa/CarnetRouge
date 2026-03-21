package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class SpecialiteResponse {

    private Long id;
    private String nom;
    private String code;
    private String description;
    private int nombreClasses; // ✅ nombre de classes dans cette spécialité
}