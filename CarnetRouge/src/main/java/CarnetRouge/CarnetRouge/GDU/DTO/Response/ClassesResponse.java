package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ClassesResponse {

    private Long id;
    private String nom;
    private String specialiteNom;
    private Long specialiteId;
}