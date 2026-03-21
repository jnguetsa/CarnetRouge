package CarnetRouge.CarnetRouge.GDU.Repository;

import CarnetRouge.CarnetRouge.GDU.Entity.PlageHoraire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface PlageHoraireRepository extends JpaRepository<PlageHoraire, Long> {

    // ── Listes ──
    List<PlageHoraire> findByClasseId(Long classeId);
    List<PlageHoraire> findByClasseIdAndJourBetween(Long classeId, LocalDate debut, LocalDate fin);
    List<PlageHoraire> findByEnseignantId(Long enseignantId);
    List<PlageHoraire> findByEnseignantIdAndJourBetween(Long enseignantId, LocalDate debut, LocalDate fin);
    List<PlageHoraire> findByUeId(Long ueId);

    // ── Conflits à la création ──
    @Query("""
        SELECT COUNT(p) > 0 FROM PlageHoraire p
        WHERE p.enseignant.id = :enseignantId
        AND p.jour = :jour
        AND p.heureDebut < :heureFin
        AND p.heureFin > :heureDebut
    """)
    boolean existsConflitEnseignant(
            @Param("enseignantId") Long enseignantId,
            @Param("jour") LocalDate jour,
            @Param("heureDebut") LocalTime heureDebut,
            @Param("heureFin") LocalTime heureFin
    );

    @Query("""
        SELECT COUNT(p) > 0 FROM PlageHoraire p
        WHERE p.classe.id = :classeId
        AND p.jour = :jour
        AND p.heureDebut < :heureFin
        AND p.heureFin > :heureDebut
    """)
    boolean existsConflitClasse(
            @Param("classeId") Long classeId,
            @Param("jour") LocalDate jour,
            @Param("heureDebut") LocalTime heureDebut,
            @Param("heureFin") LocalTime heureFin
    );

    // ── Conflits à la modification (exclut la séance en cours) ──
    @Query("""
        SELECT COUNT(p) > 0 FROM PlageHoraire p
        WHERE p.enseignant.id = :enseignantId
        AND p.id <> :id
        AND p.jour = :jour
        AND p.heureDebut < :heureFin
        AND p.heureFin > :heureDebut
    """)
    boolean existsConflitEnseignantSaufId(
            @Param("enseignantId") Long enseignantId,
            @Param("jour") LocalDate jour,
            @Param("heureDebut") LocalTime heureDebut,
            @Param("heureFin") LocalTime heureFin,
            @Param("id") Long id
    );

    @Query("""
        SELECT COUNT(p) > 0 FROM PlageHoraire p
        WHERE p.classe.id = :classeId
        AND p.id <> :id
        AND p.jour = :jour
        AND p.heureDebut < :heureFin
        AND p.heureFin > :heureDebut
    """)
    boolean existsConflitClasseSaufId(
            @Param("classeId") Long classeId,
            @Param("jour") LocalDate jour,
            @Param("heureDebut") LocalTime heureDebut,
            @Param("heureFin") LocalTime heureFin,
            @Param("id") Long id
    );
}