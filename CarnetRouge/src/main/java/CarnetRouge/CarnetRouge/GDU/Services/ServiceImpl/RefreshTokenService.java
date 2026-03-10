package CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl;

import CarnetRouge.CarnetRouge.GDU.Config.RefreshToken;
import CarnetRouge.CarnetRouge.GDU.Entity.Utilisateurs;
import CarnetRouge.CarnetRouge.GDU.Repository.RefreshTokenRepository;
import CarnetRouge.CarnetRouge.GDU.Repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    // Durée du refresh token : 7 jours
    private static final long REFRESH_TOKEN_DURATION_MS = 7 * 24 * 60 * 60 * 1000L;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UtilisateurRepository utilisateurRepository;

    // Crée ou renouvelle le refresh token d'un utilisateur
    @Transactional
    public RefreshToken createRefreshToken(String email) {
        Utilisateurs utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + email));

        // Supprime l'ancien token
        refreshTokenRepository.deleteByUtilisateur(utilisateur);
        refreshTokenRepository.flush(); // ← Force l'exécution du DELETE avant le INSERT

        RefreshToken refreshToken = RefreshToken.builder()
                .utilisateur(utilisateur)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(REFRESH_TOKEN_DURATION_MS))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    // Vérifie que le refresh token est valide et non expiré
    @Transactional
    public RefreshToken verifyExpiration(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token introuvable"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expiré, veuillez vous reconnecter");
        }

        return refreshToken;
    }

    // Supprime le refresh token (logout)
    public void deleteByUtilisateur(Utilisateurs utilisateur) {
        refreshTokenRepository.deleteByUtilisateur(utilisateur);
    }
}