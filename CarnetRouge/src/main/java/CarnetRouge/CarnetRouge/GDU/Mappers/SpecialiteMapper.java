package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.SpecialiteRequest;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.SpecialiteResponse;
import CarnetRouge.CarnetRouge.GDU.Entity.Specialite;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialiteMapper {

    // ✅ Entity → Response
    // nombreClasses est calculé depuis la taille de la collection
    @Mapping(target = "nombreClasses", expression = "java(specialite.getClasses() != null ? specialite.getClasses().size() : 0)")
    SpecialiteResponse toResponse(Specialite specialite);

    // ✅ Liste
    List<SpecialiteResponse> toResponseList(List<Specialite> specialites);

    // ✅ Request → Entity
    @Mapping(target = "id",        ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "classes",   ignore = true)
    Specialite toEntity(SpecialiteRequest request);

    // ✅ Mise à jour partielle
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",        ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "classes",   ignore = true)
    void updateFromRequest(SpecialiteRequest request, @MappingTarget Specialite specialite);
}