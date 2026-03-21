package CarnetRouge.CarnetRouge.GDU.Controller;


import CarnetRouge.CarnetRouge.GDU.DTO.Request.SpecialiteRequest;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.SpecialiteResponse;
import CarnetRouge.CarnetRouge.GDU.Entity.Specialite;
import CarnetRouge.CarnetRouge.GDU.Exception.UserNotFoundException;
import CarnetRouge.CarnetRouge.GDU.Mappers.SpecialiteMapper;
import CarnetRouge.CarnetRouge.GDU.Repository.SpecialiteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/specialites")
public class SpecialiteController {

    private final SpecialiteRepository specialiteRepository;
    private final SpecialiteMapper specialiteMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SpecialiteResponse>> lister() {
        return ResponseEntity.ok(
                specialiteMapper.toResponseList(specialiteRepository.findAll())
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpecialiteResponse> details(@PathVariable Long id) {
        return specialiteRepository.findById(id)
                .map(specialiteMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/creer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> creer(@Valid @RequestBody SpecialiteRequest request) {
        // ✅ Vérifier que le code n'existe pas déjà
        if (specialiteRepository.findByCode(request.getCode()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Une spécialité avec le code " + request.getCode() + " existe déjà");
        }
        Specialite specialite = specialiteMapper.toEntity(request);
        return ResponseEntity.ok(specialiteMapper.toResponse(specialiteRepository.save(specialite)));
    }

    @PutMapping("/modifier/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modifier(
            @PathVariable Long id,
            @Valid @RequestBody SpecialiteRequest request) {

        Specialite specialite = specialiteRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Spécialité introuvable"));

        specialiteMapper.updateFromRequest(request, specialite);
        return ResponseEntity.ok(specialiteMapper.toResponse(specialiteRepository.save(specialite)));
    }

    @DeleteMapping("/supprimer/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> supprimer(@PathVariable Long id) {
        Specialite specialite = specialiteRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Spécialité introuvable"));

        // ✅ Vérifier qu'il n'y a pas de classes liées avant de supprimer
        if (!specialite.getClasses().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Impossible de supprimer : " + specialite.getClasses().size() + " classe(s) liée(s)");
        }

        specialiteRepository.deleteById(id);
        return ResponseEntity.ok("Spécialité supprimée avec succès");
    }
}