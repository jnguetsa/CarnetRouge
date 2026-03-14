package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.ActiveRoleDTORequest;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.RoleResponseDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {PermissionMapper.class},
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper {

    RoleResponseDTO toDTO(Role role);

    ActiveRoleDTORequest toActiveRoleDTORequest(Role role);

    @Mapping(target = "name",        ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "creatAt",     ignore = true)
    @Mapping(target = "updateAt",    ignore = true)
    @Mapping(target = "permissions", ignore = true)
    void updateRoleFromDTO(ActiveRoleDTORequest activeRoleDTORequest, @MappingTarget Role role);

    // ✅ Liste
    default List<RoleResponseDTO> toDTORole(List<Role> roles) {
        return roles.stream().map(this::toDTO).toList();
    }
}