package CarnetRouge.CarnetRouge.GDU.Controller;

import CarnetRouge.CarnetRouge.GDU.DTO.Response.EnseignantAvecUEResponse;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.EnseignantResponseDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UESimpleResponse;
import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import CarnetRouge.CarnetRouge.GDU.Repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller  // ✅ pas @RestController
@RequiredArgsConstructor
@RequestMapping("/admin/enseignants")
public class EnseignantController {

    private final UtilisateurRepository utilisateurRepository;

    // ✅ Page liste des enseignants — retourne une vue Thymeleaf
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String lister(Model model) {
        List<EnseignantResponseDTO> enseignants = utilisateurRepository.findAll()
                .stream()
                .filter(u -> u instanceof Enseignant)
                .map(u -> (Enseignant) u)
                .map(this::toResponse)
                .collect(Collectors.toList());
        model.addAttribute("enseignants", enseignants);
        return "enseignants"; // ✅ retourne la vue templates/enseignants.html
    }

    // ✅ API JSON — pour FullCalendar (chips sidebar)
    // On garde @ResponseBody pour cet endpoint car FullCalendar a besoin de JSON
    @GetMapping("/api")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    public List<EnseignantAvecUEResponse> listerApi() {
        return utilisateurRepository.findAll()
                .stream()
                .filter(u -> u instanceof Enseignant)
                .map(u -> (Enseignant) u)
                .map(e -> EnseignantAvecUEResponse.builder()
                        .id(e.getId())
                        .nom(e.getNom())
                        .prenom(e.getPrenom())
                        .couleur(genererCouleur(e.getId()))
                        .ues(e.getUes().stream()
                                .map(ue -> new UESimpleResponse(ue.getId(), ue.getNom(), ue.getCode()))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    // DTO avec UEs incluses


    // ✅ Page détails d'un enseignant
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String details(@PathVariable Long id, Model model) {
        return utilisateurRepository.findById(id)
                .filter(u -> u instanceof Enseignant)
                .map(u -> (Enseignant) u)
                .map(e -> {
                    model.addAttribute("enseignant", toResponse(e));
                    return "enseignantDetails"; // ✅ templates/enseignantDetails.html
                })
                .orElse("redirect:/notFound");
    }

    // ✅ API JSON — UEs d'un enseignant pour FullCalendar



    @GetMapping("/{id}/ues")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    @ResponseBody
    @Transactional(readOnly = true)   // ← cette ligne change TOUT dans 90% des cas
    public List<UESimpleResponse> getUEsParEnseignant(@PathVariable Long id) {
        return utilisateurRepository.findById(id)
                .filter(u -> u instanceof Enseignant)
                .map(u -> (Enseignant) u)
                .map(e -> {
                    // Debug temporaire – à laisser 2 min pour tester
                    System.out.println(">>> DEBUG UES - Enseignant " + id + " → " + e.getNom() + " " + e.getPrenom());
                    System.out.println(">>> Taille collection ues : " + e.getUes().size());


                    return e.getUes().stream()
                            .map(ue -> new UESimpleResponse(ue.getId(), ue.getNom(), ue.getCode()))
                            .collect(Collectors.toList());
                })
                .orElse(List.of());
    }
    // ── Mapper interne ──
    private EnseignantResponseDTO toResponse(Enseignant e) {
        return EnseignantResponseDTO.builder()
                .id(e.getId())
                .nom(e.getNom())
                .prenom(e.getPrenom())
                .email(e.getEmail())
                .telephone(e.getTelephone())
                .grade(e.getGrade())
                .typeEnseignant(e.getTypeEnseignant())
                .actif(e.isActive())
                .couleur(genererCouleur(e.getId()))
                .build();
    }

    private String genererCouleur(Long id) {
        String[] couleurs = {
                "#dc2626", "#0f4c75", "#1f6f3c", "#5b21b6",
                "#7c2d12", "#6d3078", "#7d6608", "#155e75",
                "#374151", "#1a5276", "#1b4332", "#4a235a"
        };
        return couleurs[(int)(id % couleurs.length)];
    }
}