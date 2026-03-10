package CarnetRouge.CarnetRouge.GDU.Repository;

import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

   Page<Enseignant> findByTypeEnseignant(String typeEnseignant, Pageable pageable);

   Page<Enseignant> findByEmailContainingIgnoreCase(String email, Pageable pageable);

 Page<Enseignant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);
   boolean existsByEmail(String email);

   Optional<Enseignant> findByEmail(String email);


}