package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class EvenementResponse {

    private Long id;
    private String nom;
    private String description;
    private String couleur;
    private Long plageHoraireId;
}