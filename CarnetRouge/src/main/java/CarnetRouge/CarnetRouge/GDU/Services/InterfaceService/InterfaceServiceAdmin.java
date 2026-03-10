package CarnetRouge.CarnetRouge.GDU.Services.InterfaceService;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.AssistantRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.EnseignantRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UtilisateursDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Assistant;
import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import org.springframework.data.domain.Page;


public interface InterfaceServiceAdmin {

  void  activerDesactiverUtilisateur(Long id,  boolean activer);


    Page<UtilisateursDTO> listeTous(String recherche,String type, int page, int size);

    void  deleteUtilisateur(Long id);

    Enseignant getById(Long id) ;

  Enseignant save(EnseignantRequestDTO enseignantRequestDTO);

  Assistant saveAssistant(AssistantRequestDTO assistantRequestDTO);
}
