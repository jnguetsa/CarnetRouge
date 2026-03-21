package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ClassesRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotNull(message = "La spécialité est obligatoire")
    private Long specialiteId;
}