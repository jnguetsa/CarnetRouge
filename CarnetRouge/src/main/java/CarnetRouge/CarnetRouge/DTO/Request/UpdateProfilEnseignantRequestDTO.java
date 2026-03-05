package CarnetRouge.CarnetRouge.DTO.Request;

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

    protected Long id;
    @NotBlank(message = "Le prenom est obligatoire")

    protected String nom;
    @NotBlank(message = "Le prenom est obligatoire")
    protected String prenom;
    @Column(unique = true, nullable = false)
    protected String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    protected String password;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date dateNaissance;
    @NotBlank(message = "Renseigner ce Champ")
    protected  String telephone;
}
