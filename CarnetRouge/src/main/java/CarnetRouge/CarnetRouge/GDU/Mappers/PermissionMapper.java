package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.PermissionRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.PermissionResponseDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" , unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    PermissionResponseDTO toDTO(Permission permission);


    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "creatAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    Permission toEntity(PermissionRequestDTO permissionRequestDTO);

    default List<PermissionResponseDTO> toDTO(List<Permission> permissions) {
        return permissions.stream().map(this::toDTO).toList();
    }


}
