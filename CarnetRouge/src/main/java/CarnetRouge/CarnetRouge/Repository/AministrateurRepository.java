package CarnetRouge.CarnetRouge.Repository;

import CarnetRouge.CarnetRouge.Entity.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AministrateurRepository extends JpaRepository<Administrateur, Long> {


    Optional<Administrateur> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Administrateur> findByFonction(String fonction);

}
