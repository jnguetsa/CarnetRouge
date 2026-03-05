package CarnetRouge.CarnetRouge.Repository;

import CarnetRouge.CarnetRouge.Entity.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Page<Etudiant> findEtudiantByActive(Pageable pageable, boolean active);

    Optional<Etudiant> findByEmailIgnoreCase(String email);
    Optional<Etudiant> findByMatricule(String matricule);
    Page<Etudiant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
            String nom, String prenom, Pageable pageable);

    Optional<Etudiant> findByEmailContainingIgnoreCase(String email);
    boolean existsByEmailContainingIgnoreCase(String email);
}
