package CarnetRouge.CarnetRouge.GDU.Repository;

import CarnetRouge.CarnetRouge.GDAE.Entity.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UERepository extends JpaRepository<UE, Long> {

    // ✅ Trouver une UE par son code (ex: "INF301")
    Optional<UE> findByCode(String code);

    // ✅ Trouver toutes les UE d'un enseignant
    List<UE> findByEnseignantsId(Long enseignantId);

    // ✅ Trouver toutes les UE d'une classe
    List<UE> findByClassesId(Long classeId);

    // ✅ Recherche par nom
    List<UE> findByNomContainingIgnoreCase(String nom);
}