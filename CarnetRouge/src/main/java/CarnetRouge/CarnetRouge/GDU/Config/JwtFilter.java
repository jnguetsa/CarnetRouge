package CarnetRouge.CarnetRouge.GDU.Config;

import CarnetRouge.CarnetRouge.GDU.Entity.Utilisateurs;
import CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService  refreshTokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractCookie(request, "JWT_TOKEN");

        // JWT absent ou expiré → tente le refresh
        if (token == null) {
            String refreshToken = extractCookie(request, "REFRESH_TOKEN");

            if (refreshToken != null) {
                try {
                    // Vérifie le refresh token
                    RefreshToken rt = refreshTokenService.verifyExpiration(refreshToken);
                    Utilisateurs utilisateur = rt.getUtilisateur();

                    // Génère un nouveau JWT
                    String newJwt = jwtService.generateJwtToken(utilisateur);

                    // Pose le nouveau cookie JWT (15 min)
                    String cookie = String.format(
                            "JWT_TOKEN=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Strict",
                            newJwt, 15 * 60
                    );
                    response.addHeader("Set-Cookie", cookie);

                    // Authentifie pour cette requête
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    utilisateur, null, utilisateur.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("🔄 JWT renouvelé via refresh token pour : " + utilisateur.getEmail());

                } catch (Exception e) {
                    System.out.println("❌ Refresh token invalide : " + e.getMessage());
                    // Refresh invalide → laisse Spring Security rediriger vers /login
                }
            }

            filterChain.doFilter(request, response);
            return;
        }

        // JWT présent → validation normale
        try {
            String email = jwtService.extractUsername(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            // JWT expiré → sera géré au prochain appel via REFRESH_TOKEN
            System.out.println("⏰ JWT expiré pour, tentative refresh au prochain appel");
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}