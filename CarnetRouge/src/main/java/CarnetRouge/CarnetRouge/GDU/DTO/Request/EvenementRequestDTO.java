package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class EvenementRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;
    private String couleur;

    @NotNull(message = "La plage horaire est obligatoire")
    private Long plageHoraireId;
}