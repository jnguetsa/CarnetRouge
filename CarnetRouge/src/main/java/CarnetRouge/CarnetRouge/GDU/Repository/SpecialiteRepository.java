package CarnetRouge.CarnetRouge.GDU.Repository;

import CarnetRouge.CarnetRouge.GDAE.Entity.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {

    // ✅ Recherche par code
    Optional<Specialite> findByCode(String code);

    // ✅ Recherche par nom
    List<Specialite> findByNomContainingIgnoreCase(String nom);
}