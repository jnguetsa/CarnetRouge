package CarnetRouge.CarnetRouge.GDU.Mappers;

import CarnetRouge.CarnetRouge.GDU.DTO.Response.PlageHoraireResponse;
import CarnetRouge.CarnetRouge.GDET.Entity.PlageHoraire;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlageHoraireMapper {

    @Mapping(source = "ue.id",             target = "ueId")          // ✅
    @Mapping(source = "enseignant.id",     target = "enseignantId")  // ✅
    @Mapping(source = "classe.id",         target = "classeId")
    @Mapping(source = "classe.nom",        target = "classeNom")
    @Mapping(source = "ue.nom",            target = "ueNom")
    @Mapping(source = "ue.code",           target = "ueCode")
    @Mapping(source = "enseignant.nom",    target = "enseignantNom")
    @Mapping(source = "enseignant.prenom", target = "enseignantPrenom")
    PlageHoraireResponse toResponse(PlageHoraire plageHoraire);

    // ✅ Liste
    java.util.List<PlageHoraireResponse> toResponseList(List<PlageHoraire> plageHoraires);
}