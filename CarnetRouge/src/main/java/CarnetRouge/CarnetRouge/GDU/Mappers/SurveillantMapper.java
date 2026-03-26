package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Response.SurveillantResponseDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.SurveillantResponseDetails;
import CarnetRouge.CarnetRouge.GDU.Entity.Surveillant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class},
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SurveillantMapper {

    SurveillantResponseDTO toDTO(Surveillant surveillant);
    SurveillantResponseDetails toDtoDetails(Surveillant surveillant);
}