package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePasswordRequestDTO {
    @NotBlank(message = "Le mot de passe est obligatoire")
    protected String password;

}