package CarnetRouge.CarnetRouge.GDU.Repository;

import CarnetRouge.CarnetRouge.GDU.Config.RefreshToken;
import CarnetRouge.CarnetRouge.GDU.Entity.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    @Modifying(clearAutomatically = true)
    void deleteByUtilisateur(Utilisateurs utilisateur);
}
