package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfilEnseignantRequestDTO {
    private Long id; // Optionnel si l'ID est dans l'URL (PUT /1)

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private Date dateNaissance;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    private String grade;
    private String typeEnseignant;
}

