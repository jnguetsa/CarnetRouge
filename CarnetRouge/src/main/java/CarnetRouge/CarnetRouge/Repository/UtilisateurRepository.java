package CarnetRouge.CarnetRouge.Repository;

import CarnetRouge.CarnetRouge.Entity.Utilisateurs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateurs, Long> {

    Optional<Utilisateurs> findByEmail(String email);
    boolean existsByEmail(String email);

    // ✅ Active est un boolean → il faut préciser True ou False
    Page<Utilisateurs> findByActiveTrue(Pageable pageable);
    Page<Utilisateurs> findByActiveFalse(Pageable pageable);

    // ✅ Le paramètre de recherche doit venir AVANT Pageable
    Page<Utilisateurs> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    Page<Utilisateurs> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    Page<Utilisateurs> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
            String nom, String prenom, Pageable pageable);
}