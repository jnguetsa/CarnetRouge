package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.UERequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UEResponse;
import CarnetRouge.CarnetRouge.GDAE.Entity.UE;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UEMapper {

    // ✅ Entity → Response
    UEResponse toResponse(UE ue);

    // ✅ Liste
    List<UEResponse> toResponseList(List<UE> ues);

    // ✅ Request → Entity (ignore les champs auto-générés)
    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    @Mapping(target = "plagesHoraires", ignore = true)
    UE toEntity(UERequestDTO request);

    // ✅ Mise à jour partielle
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    @Mapping(target = "plagesHoraires", ignore = true)
    @Mapping(target = "enseignants",    ignore = true)
    @Mapping(target = "classes",        ignore = true)
    void updateFromRequest(UERequestDTO request, @MappingTarget UE ue);
}