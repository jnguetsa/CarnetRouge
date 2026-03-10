package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.AssistantRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.AssistantResponse;
import CarnetRouge.CarnetRouge.GDU.Entity.Assistant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class}, unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssistantMappers {

    AssistantResponse toDTO(Assistant assistant);

    void ajoutAssistant(AssistantRequestDTO assistantRequestDTO,@MappingTarget Assistant assistant);

    Assistant toentity(AssistantRequestDTO assistantRequestDTO);



    default List<AssistantResponse> toDTO(List<Assistant> assistants) {
        return assistants.stream().map(this::toDTO).toList();
    }

}
