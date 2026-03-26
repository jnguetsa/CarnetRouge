package CarnetRouge.CarnetRouge.GDU.Repository;

import CarnetRouge.CarnetRouge.GDET.Entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {

    // ✅ Trouver toutes les classes d'une spécialité
    List<Classes> findBySpecialiteId(Long specialiteId);

    // ✅ Recherche par nom
    List<Classes> findByNomContainingIgnoreCase(String nom);
    Optional<Classes> findByNom(String nom);
}