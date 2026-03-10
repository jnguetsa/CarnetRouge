package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Response.RoleResponseDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class}, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleResponseDTO toDTO(Role role);

    default List<RoleResponseDTO> toDTORole(List<Role> roles) {
        return roles.stream().map(this::toDTO).toList();
    }
}
