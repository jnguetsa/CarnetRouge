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
public class UEAvecEnseignantsResponse {

    private Long id;
    private String nom;
    private String code;
    private List<EnseignantSimpleResponse> enseignants;
}
