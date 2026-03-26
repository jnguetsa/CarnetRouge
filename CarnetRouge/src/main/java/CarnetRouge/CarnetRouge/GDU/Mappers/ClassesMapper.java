package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.ClassesRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.ClassesResponse;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.ClassesResponseDTO;
import CarnetRouge.CarnetRouge.GDET.Entity.Classes;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassesMapper {

    @Mapping(source = "specialite.id",  target = "specialiteId")
    @Mapping(source = "specialite.nom", target = "specialiteNom")
    ClassesResponse toResponse(Classes classes);

    List<ClassesResponse> toResponseList(List<Classes> classes);

    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    @Mapping(target = "ue",             ignore = true)
    @Mapping(target = "specialite",     ignore = true)
    @Mapping(target = "plagesHoraires", ignore = true)
    Classes toEntity(ClassesRequestDTO request);

    // ✅ Méthode que MapStruct trouve automatiquement via uses = {ClassesMapper.class}
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "nom", source = "nom")
    ClassesResponseDTO map(Classes classes);

    // ✅ Liste
    List<ClassesResponseDTO> mapList(List<Classes> classes);
}