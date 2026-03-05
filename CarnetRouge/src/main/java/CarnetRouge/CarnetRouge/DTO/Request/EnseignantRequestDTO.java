package CarnetRouge.CarnetRouge.DTO.Request;

import CarnetRouge.CarnetRouge.Enum.STypeEnseignant;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

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

    // Pour un mot de passe en création → souvent plus strict
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    // @Pattern peut être ajouté pour plus de complexité si besoin
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

