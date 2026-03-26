package CarnetRouge.CarnetRouge.GDU.Repository;
import CarnetRouge.CarnetRouge.GDU.Entity.Utilisateurs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateurs, Long> {

    @Query("SELECT u FROM Utilisateurs u WHERE TYPE(u) IN (Enseignant, Assistant, Surveillant) " +
            "AND (:type = 'TOUS' OR " +
            "    (:type = 'ENS' AND TYPE(u) = Enseignant) OR " +
            "    (:type = 'ASS' AND TYPE(u) = Assistant) OR " +
            "    (:type = 'SUR' AND TYPE(u) = Surveillant)) " +
            "AND (:recherche IS NULL OR :recherche = '' OR " +
            "    LOWER(u.nom) LIKE LOWER(CONCAT('%', :recherche, '%')) OR " +
            "    LOWER(u.prenom) LIKE LOWER(CONCAT('%', :recherche, '%')))")
    Page<Utilisateurs> searchWithFilters(
            @Param("recherche") String recherche,
            @Param("type") String type,
            Pageable pageable
    );

    Optional<Utilisateurs> findByEmail(String email);
        boolean existsByEmail(String email);

}