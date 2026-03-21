package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class EnseignantRequestDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String grade;
    private String typeEnseignant;
    private boolean actif;
    private String couleur;
}
