package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ActiveUtilisateurRequestDTO {
       private Long id;
       private  boolean active;
}
