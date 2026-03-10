package CarnetRouge.CarnetRouge.GDU.Repository;

import CarnetRouge.CarnetRouge.GDU.Entity.Assistant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssistantRepository extends JpaRepository<Assistant,Long> {
    boolean existsByEmail(String email);
    Page<Assistant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);
    Optional<Assistant> findByEmail(String email);
}
