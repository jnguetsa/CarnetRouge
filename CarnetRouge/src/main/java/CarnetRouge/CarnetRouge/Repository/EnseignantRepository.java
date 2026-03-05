package CarnetRouge.CarnetRouge.Repository;

import CarnetRouge.CarnetRouge.Entity.Enseignant;
import CarnetRouge.CarnetRouge.Entity.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
   Page<Enseignant> findByTypeEnseignant(String typeEnseignant, Pageable pageable);
   Page<Enseignant> findByEmailIgnoreCase(String email, Pageable ptageable);
   Page<Enseignant> findByNomContainingIgnoreCase(String nom, Pageable pageable);

   Optional<Enseignant>findByEmailContainingIgnoreCase(String email);
   boolean existsByEmailContainingIgnoreCase(String email);


   Optional<Enseignant> findByEmail(String mail);
}
