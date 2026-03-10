package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class UpdateProfileEtudiantRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "le matricule est obligatoire")
    private  String matricule;
    private Date dateNaissance;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;
}
