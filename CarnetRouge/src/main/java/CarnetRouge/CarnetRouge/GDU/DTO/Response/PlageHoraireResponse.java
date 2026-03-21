package CarnetRouge.CarnetRouge.GDU.DTO.Response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class PlageHoraireResponse {

    private Long id;
    private LocalDate jour;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String salle;
    private String couleur;

    // Infos classe
    private Long classeId;
    private String classeNom;

    // Infos UE
    private Long ueId;
    private String ueNom;
    private String ueCode;

    // Infos enseignant
    private Long enseignantId;
    private String enseignantNom;
    private String enseignantPrenom;


    // ✅ Format FullCalendar
    public String getTitle() {
        return ueNom + " — " + enseignantPrenom + " " + enseignantNom;
    }

    public String getStart() {
        return jour + "T" + heureDebut;
    }

    public String getEnd() {
        return jour + "T" + heureFin;
    }
}