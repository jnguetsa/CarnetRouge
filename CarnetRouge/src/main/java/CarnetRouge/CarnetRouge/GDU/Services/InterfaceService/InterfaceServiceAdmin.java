package CarnetRouge.CarnetRouge.GDU.Services.InterfaceService;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.ActivePermissionRequest;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.ActiveRoleDTORequest;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.AssistantRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.EnseignantRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.AssistantResponseDetails;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.EnseignantResponseDetails;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UtilisateursDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Assistant;
import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


public interface InterfaceServiceAdmin {

  void  activerDesactiverUtilisateur(Long id,  boolean activer);


    Page<UtilisateursDTO> listeTous(String recherche,String type, int page, int size);

  UtilisateursDTO findById(Long id);


    void  deleteUtilisateur(Long id);

    Enseignant getById(Long id) ;

  Enseignant save(EnseignantRequestDTO enseignantRequestDTO);

  Assistant saveAssistant(AssistantRequestDTO assistantRequestDTO);

    EnseignantResponseDetails EnsDetails(Long id);

  @Transactional
  AssistantResponseDetails AssDetails(Long id);

  @Transactional
  ActiveRoleDTORequest activeRole(Long id, ActiveRoleDTORequest activeRoleDTORequest);

  @Transactional
  ActivePermissionRequest activePermissionRequest(Long id, ActivePermissionRequest activePermissionRequest);
}
