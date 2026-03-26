package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.EvenementRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.EvenementResponse;
import CarnetRouge.CarnetRouge.GDET.Entity.Evenement;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvenementMapper {

    @Mapping(source = "plageHoraire.id", target = "plageHoraireId")
    EvenementResponse toResponse(Evenement evenement);

    List<EvenementResponse> toResponseList(List<Evenement> evenements);

    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "createdAt",    ignore = true)
    @Mapping(target = "updatedAt",    ignore = true)
    @Mapping(target = "plageHoraire", ignore = true)
    Evenement toEntity(EvenementRequestDTO request);
}