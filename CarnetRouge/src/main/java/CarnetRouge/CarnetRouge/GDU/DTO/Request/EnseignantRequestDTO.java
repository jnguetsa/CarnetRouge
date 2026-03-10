package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import CarnetRouge.CarnetRouge.GDU.Enum.STypeEnseignant;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnseignantRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;

    @NotBlank(message = "Le grade est obligatoire")
    private String grade;

    @NotBlank(message = "Le type d'enseignant est obligatoire")
    @Pattern(regexp = "PERMANENT|VACATAIRE|CONTRACTUEL")
    @Enumerated(EnumType.STRING)
    private STypeEnseignant typeEnseignant;
    @NotBlank(message = "Le numero de telephone de enseignant est obligatoire")
    @Max(9)
    @Min(9)
    private String telephone;

}

