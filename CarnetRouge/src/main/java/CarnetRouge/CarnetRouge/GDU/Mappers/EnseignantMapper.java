package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.EnseignantRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.UpdatePasswordRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.UpdateProfilEnseignantRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.EnseignantResponseDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.EnseignantResponseDetails;
import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EnseignantMapper {

    EnseignantResponseDTO toDTO(Enseignant entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateProfile(UpdateProfilEnseignantRequestDTO dto, @MappingTarget Enseignant entity);

    @Mapping(target = "password", source = "password")
    void updatePassword(UpdatePasswordRequestDTO dto, @MappingTarget Enseignant entity);

    void ajouterEnseignant(EnseignantRequestDTO enseignantRequestDTO, @MappingTarget Enseignant enseignant);

    default List<EnseignantResponseDTO> toDTOList(List<Enseignant> enseignants) {
        return enseignants.stream()
                .map(this::toDTO)
                .toList();
    }
    EnseignantResponseDetails toDtoDetails(Enseignant enseignant);
}

