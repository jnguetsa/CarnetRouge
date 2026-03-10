package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.UpdatePasswordRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.UpdateProfileEtudiantRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.EtudiantResponseDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Etudiant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class},  unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface EtudiantMapper {
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "password", ignore = true)
        @Mapping(target = "roles", ignore = true)
        @Mapping(target = "active", ignore = true)
        void updateProfile(UpdateProfileEtudiantRequestDTO dto, @MappingTarget Etudiant etudiant);

        @Mapping(target = "password", source = "password")
        void updatePassword(UpdatePasswordRequestDTO updatePasswordRequestDTO, @MappingTarget Etudiant etudiant);

        default List<EtudiantResponseDTO> toDTOList(List<Etudiant> etudiants) {
                return etudiants.stream()
                        .map(this::toDTO)
                        .toList();
        }

        EtudiantResponseDTO toDTO(Etudiant etudiant);

        void ajouterEtudiant(EtudiantResponseDTO etudiantResponseDTO,@MappingTarget Etudiant etudiant);
}
