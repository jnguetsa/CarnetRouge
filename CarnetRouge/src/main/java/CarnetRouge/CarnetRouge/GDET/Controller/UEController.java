package CarnetRouge.CarnetRouge.GDET.Controller;


import CarnetRouge.CarnetRouge.GDU.DTO.Request.UERequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UEResponse;
import CarnetRouge.CarnetRouge.GDAE.Entity.UE;
import CarnetRouge.CarnetRouge.GDU.Mappers.UEMapper;
import CarnetRouge.CarnetRouge.GDU.Repository.UERepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ues")
public class UEController {

    private final UERepository ueRepository;
    private final UEMapper ueMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UEResponse>> lister() {
        return ResponseEntity.ok(
                ueMapper.toResponseList(ueRepository.findAll())
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UEResponse> details(@PathVariable Long id) {
        return ueRepository.findById(id)
                .map(ueMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/creer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UEResponse> creer(@Valid @RequestBody UERequestDTO request) {
        UE ue = ueMapper.toEntity(request);
        return ResponseEntity.ok(ueMapper.toResponse(ueRepository.save(ue)));
    }

    @PutMapping("/modifier/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UEResponse> modifier(
            @PathVariable Long id,
            @Valid @RequestBody UERequestDTO request) {
        return ueRepository.findById(id)
                .map(ue -> {
                    ueMapper.updateFromRequest(request, ue);
                    return ResponseEntity.ok(ueMapper.toResponse(ueRepository.save(ue)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/supprimer/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!ueRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ueRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
