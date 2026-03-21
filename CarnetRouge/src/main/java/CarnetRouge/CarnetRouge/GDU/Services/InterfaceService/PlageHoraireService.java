package CarnetRouge.CarnetRouge.GDU.Services.InterfaceService;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.PlageHoraireRequest;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.PlageHoraireResponse;

import java.time.LocalDate;
import java.util.List;

public interface PlageHoraireService {

    // CRUD
    PlageHoraireResponse creer(PlageHoraireRequest request);
    PlageHoraireResponse modifier(Long id, PlageHoraireRequest request);
    void supprimer(Long id);
    PlageHoraireResponse findById(Long id);

    // EDT par classe
    List<PlageHoraireResponse> getEdtClasse(Long classeId);
    List<PlageHoraireResponse> getEdtClasseSemaine(Long classeId, LocalDate debut, LocalDate fin);

    // EDT par enseignant
    List<PlageHoraireResponse> getEdtEnseignant(Long enseignantId);
    List<PlageHoraireResponse> getEdtEnseignantSemaine(Long enseignantId, LocalDate debut, LocalDate fin);

    // Pour FullCalendar
    List<PlageHoraireResponse> getEdtClassePourCalendrier(Long classeId, LocalDate debut, LocalDate fin);
}