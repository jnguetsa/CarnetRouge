package CarnetRouge.CarnetRouge.GDU.Controller;


import CarnetRouge.CarnetRouge.GDU.DTO.Request.PlageHoraireRequest;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.PlageHoraireResponse;
import CarnetRouge.CarnetRouge.GDU.Repository.ClassesRepository;
import CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl.PlageHoraireServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/emplois-du-temps")
public class PlageHoraireController {

    private final PlageHoraireServiceImpl plageHoraireService;
    private  final ClassesRepository  classesRepository;

    // ── PAGE PRINCIPALE EDT ──

    @GetMapping
    public String pageEdt(Model model) {
        model.addAttribute("classes", classesRepository.findAll()); // ✅
        return "emploisDeTemps";
    }

    @GetMapping("/api/classe/{classeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT', 'ETUDIANT')")
    @ResponseBody
    public ResponseEntity<List<PlageHoraireResponse>> getEdtClasse(
            @PathVariable Long classeId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<PlageHoraireResponse> result;

        if (debut != null && fin != null) {
            // ✅ EDT d'une classe pour une semaine précise
            result = plageHoraireService.getEdtClasseSemaine(classeId, debut, fin);
        } else {
            // ✅ Tout l'EDT de la classe
            result = plageHoraireService.getEdtClasse(classeId);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/enseignant/{enseignantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    @ResponseBody
    public ResponseEntity<List<PlageHoraireResponse>> getEdtEnseignant(
            @PathVariable Long enseignantId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<PlageHoraireResponse> result;

        if (debut != null && fin != null) {
            result = plageHoraireService.getEdtEnseignantSemaine(enseignantId, debut, fin);
        } else {
            result = plageHoraireService.getEdtEnseignant(enseignantId);
        }

        return ResponseEntity.ok(result);
    }

    // ── CRUD ──

    @PostMapping("/creer")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> creer(@Valid @RequestBody PlageHoraireRequest request) {
        try {
            PlageHoraireResponse response = plageHoraireService.creer(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/modifier/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> modifier(
            @PathVariable Long id,
            @Valid @RequestBody PlageHoraireRequest request) {
        try {
            PlageHoraireResponse response = plageHoraireService.modifier(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/supprimer/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> supprimer(@PathVariable Long id) {
        try {
            plageHoraireService.supprimer(id);
            return ResponseEntity.ok("Séance supprimée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    @ResponseBody
    public ResponseEntity<PlageHoraireResponse> details(@PathVariable Long id) {
        return ResponseEntity.ok(plageHoraireService.findById(id));
    }
}