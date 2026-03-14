package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.ActiveUtilisateurRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UtilisateursDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Assistant;
import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import CarnetRouge.CarnetRouge.GDU.Entity.Utilisateurs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class},
        imports = {Enseignant.class, Assistant.class}  ,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UtilisateurMapper {

    @Mapping(target = "type", expression = "java(utilisateur instanceof Enseignant ? \"ENS\" : \"ASS\")")
    @Mapping(target = "grade",  expression = "java(utilisateur instanceof Enseignant ? ((Enseignant) utilisateur).getGrade() : null)")
    @Mapping(target = "typeEnseignant",expression = "java(utilisateur instanceof Enseignant ? ((Enseignant) utilisateur).getTypeEnseignant() : null)")
    @Mapping(target = "fonction", expression = "java(utilisateur instanceof Assistant  ? ((Assistant)  utilisateur).getFonction() : null)")
    UtilisateursDTO toDTO(Utilisateurs utilisateur);

    default List<UtilisateursDTO> toDTOList(List<Utilisateurs> utilisateurs) {
        return utilisateurs.stream()
                .map(this::toDTO)
                .toList();
    }

    void activateToEntity(ActiveUtilisateurRequestDTO activeUtilisateurRequestDTO, @MappingTarget Utilisateurs utilisateur);


}