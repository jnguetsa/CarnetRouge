package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnseignantAvecUEResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String couleur;
    private List<UESimpleResponse> ues; // ✅ UEs directement incluses
}

