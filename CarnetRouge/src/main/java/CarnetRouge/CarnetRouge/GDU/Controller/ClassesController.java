package CarnetRouge.CarnetRouge.GDU.Controller;


import CarnetRouge.CarnetRouge.GDU.DTO.Request.ClassesRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.ClassesResponse;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.EnseignantSimpleResponse;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UEAvecEnseignantsResponse;
import CarnetRouge.CarnetRouge.GDU.Entity.Classes;
import CarnetRouge.CarnetRouge.GDU.Entity.Specialite;
import CarnetRouge.CarnetRouge.GDU.Exception.UserNotFoundException;
import CarnetRouge.CarnetRouge.GDU.Mappers.ClassesMapper;
import CarnetRouge.CarnetRouge.GDU.Repository.ClassesRepository;
import CarnetRouge.CarnetRouge.GDU.Repository.SpecialiteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/classes")
public class ClassesController {

    private final ClassesRepository classesRepository;
    private final SpecialiteRepository specialiteRepository;
    private final ClassesMapper classesMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClassesResponse>> lister() {
        return ResponseEntity.ok(
                classesMapper.toResponseList(classesRepository.findAll())
        );
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassesResponse> details(@PathVariable Long id) {
        return classesRepository.findById(id)
                .map(classesMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/creer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassesResponse> creer(@Valid @RequestBody ClassesRequestDTO request) {
        Specialite specialite = specialiteRepository.findById(request.getSpecialiteId())
                .orElseThrow(() -> new UserNotFoundException("Spécialité introuvable"));

        Classes classe = classesMapper.toEntity(request);
        classe.setSpecialite(specialite); // ✅ setter après mapping

        return ResponseEntity.ok(classesMapper.toResponse(classesRepository.save(classe)));
    }

    @PutMapping("/modifier/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassesResponse> modifier(
            @PathVariable Long id,
            @Valid @RequestBody ClassesRequestDTO request) {

        Classes classe = classesRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Classe introuvable"));

        Specialite specialite = specialiteRepository.findById(request.getSpecialiteId())
                .orElseThrow(() -> new UserNotFoundException("Spécialité introuvable"));

        classe.setNom(request.getNom());
        classe.setSpecialite(specialite);

        return ResponseEntity.ok(classesMapper.toResponse(classesRepository.save(classe)));
    }

    @DeleteMapping("/supprimer/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!classesRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        classesRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}/ues-enseignants")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT', 'ETUDIANT')")
    public ResponseEntity<List<UEAvecEnseignantsResponse>> getUEsEnseignantsParClasse(
            @PathVariable Long id) {

        Classes classe = classesRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Classe introuvable"));

        List<UEAvecEnseignantsResponse> result = classe.getUe().stream()
                .map(ue -> UEAvecEnseignantsResponse.builder()
                        .id(ue.getId())
                        .nom(ue.getNom())
                        .code(ue.getCode())
                        .enseignants(ue.getEnseignants().stream()
                                .map(e -> new EnseignantSimpleResponse(
                                        e.getId(),
                                        e.getNom(),
                                        e.getPrenom(),
                                        genererCouleur(e.getId())
                                ))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
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