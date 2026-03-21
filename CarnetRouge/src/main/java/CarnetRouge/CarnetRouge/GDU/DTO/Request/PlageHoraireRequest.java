package CarnetRouge.CarnetRouge.GDU.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class PlageHoraireRequest {

    @NotNull(message = "Le jour est obligatoire")
    private LocalDate jour;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;

    private String salle;
    private String couleur;

    @NotNull(message = "La classe est obligatoire")
    private Long classeId;

    @NotNull(message = "L'UE est obligatoire")
    private Long ueId;

    @NotNull(message = "L'enseignant est obligatoire")
    private Long enseignantId;
}