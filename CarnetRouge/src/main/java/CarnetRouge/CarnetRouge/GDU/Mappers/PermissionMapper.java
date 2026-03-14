package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.ActivePermissionRequest;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.PermissionRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.PermissionResponseDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" )
public interface PermissionMapper {

    PermissionResponseDTO toDTO(Permission permission);

//--------------------------
    ActivePermissionRequest toActivePermmission(Permission permission);


    //--

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateAt",    ignore = true)
    void  updatePermission(ActivePermissionRequest activePermissionRequest, @MappingTarget Permission  permission );

    //--------------
    default List<PermissionResponseDTO> toDTO(List<Permission> permissions) {
        return permissions.stream().map(this::toDTO).toList();
    }




}
