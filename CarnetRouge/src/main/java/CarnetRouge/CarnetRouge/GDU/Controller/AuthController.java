package CarnetRouge.CarnetRouge.GDU.Controller;

import CarnetRouge.CarnetRouge.GDU.Config.CookieUtils;
import CarnetRouge.CarnetRouge.GDU.Config.JwtService;
import CarnetRouge.CarnetRouge.GDU.Config.RefreshToken;
import CarnetRouge.CarnetRouge.GDU.Entity.Utilisateurs;
import CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    // ── Pages ──

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/notFound")
    public String notFoundPage() {
        return "notFound";
    }


    // ── Dashboards ──

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "dashboard";
    }

    @GetMapping("/enseignant/dashboard")
    public String enseignantDashboard() {
        return "dashboard";
    }

    @GetMapping("/etudiant/dashboard")
    public String etudiantDashboard() {
        return "dashboard";
    }

    // ── Authentification ──

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            Utilisateurs utilisateur = (Utilisateurs) authentication.getPrincipal();

            String jwtToken = jwtService.generateJwtToken(utilisateur);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(utilisateur.getEmail());

            addCookie(response, "JWT_TOKEN", jwtToken, 15 * 60);
            addCookie(response, "REFRESH_TOKEN", refreshToken.getToken(), 7 * 24 * 60 * 60);

            return determineTargetUrl(utilisateur.getAuthorities());

        } catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("error", "Identifiants invalides");
            return "redirect:/login";
        }
    }

    @GetMapping("/refresh-token")
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenValue = CookieUtils.extractCookie(request, "REFRESH_TOKEN");

        if (refreshTokenValue == null) {
            return "redirect:/login?expired=true";
        }

        try {
            RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenValue);
            Utilisateurs utilisateur = refreshToken.getUtilisateur();

            String newJwt = jwtService.generateJwtToken(utilisateur);
            addCookie(response, "JWT_TOKEN", newJwt, 15 * 60);

            return determineTargetUrl(utilisateur.getAuthorities());

        } catch (Exception e) {
            deleteCookie(response, "REFRESH_TOKEN");
            return "redirect:/login?expired=true";
        }
    }


    private String determineTargetUrl(Collection<? extends GrantedAuthority> authorities) {
        String role = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .findFirst()
                .orElse("");

        return switch (role) {
            case "ROLE_ADMIN"      -> "redirect:/admin/dashboard";
            case "ROLE_ENSEIGNANT" -> "redirect:/enseignant/dashboard";
            case "ROLE_ETUDIANT"   -> "redirect:/etudiant/dashboard";
            default                -> "redirect:/home";
        };
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)   // true en production
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}