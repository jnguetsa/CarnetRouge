package CarnetRouge.CarnetRouge.GDU.Repository;

import CarnetRouge.CarnetRouge.GDU.Entity.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Page<Etudiant> findEtudiantByActive(boolean active, Pageable pageable);

    Optional<Etudiant> findByEmail(String email);
    Page<Etudiant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);

    boolean existsByEmailContainingIgnoreCase(String email);
}
