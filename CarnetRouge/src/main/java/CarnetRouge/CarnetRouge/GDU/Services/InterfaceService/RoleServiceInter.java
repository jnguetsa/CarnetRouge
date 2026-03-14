package CarnetRouge.CarnetRouge.GDU.Services.InterfaceService;

import CarnetRouge.CarnetRouge.GDU.DTO.Response.RoleResponseDTO;

import java.util.List;

public interface RoleServiceInter {
    List<RoleResponseDTO> findAll();
    RoleResponseDTO findById(Long id);

}
