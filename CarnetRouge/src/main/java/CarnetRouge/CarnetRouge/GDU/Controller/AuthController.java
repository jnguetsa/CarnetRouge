package CarnetRouge.CarnetRouge.GDU.Controller;

import CarnetRouge.CarnetRouge.GDU.Config.JwtService;
import CarnetRouge.CarnetRouge.GDU.Config.RefreshToken;
import CarnetRouge.CarnetRouge.GDU.Entity.Utilisateurs;
import CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/notFound")
    public String notFoundPage() {

        return "notFound";
    }


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

            // 1. Génère le JWT (courte durée : 15 min)
            String jwtToken = jwtService.generateJwtToken(utilisateur);

            // 2. Génère le Refresh Token (longue durée : 7 jours)
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(utilisateur.getEmail());

            // 3. Pose les deux cookies
            addCookie(response, "JWT_TOKEN", jwtToken, 15 * 60); // 15 min
            addCookie(response, "REFRESH_TOKEN", refreshToken.getToken(), 7 * 24 * 60 * 60); // 7 jours

            return determineTargetUrl(utilisateur.getAuthorities());

        } catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("error", "Identifiants invalides");
            return "redirect:/login";
        }
    }
    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)        // true en production (HTTPS)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(false)        // true en production (HTTPS)
                .path("/")
                .maxAge(0)            // ← expire immédiatement
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
    private String determineTargetUrl(Collection<? extends GrantedAuthority> authorities) {
        String role = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .findFirst()
                .orElse("");

        return switch (role) {
            case "ROLE_ADMIN" -> "redirect:/admin/dashboard";
            case "ROLE_ENSEIGNANT" -> "redirect:/enseignant/dashboard";
            case "ROLE_ETUDIANT" -> "redirect:/etudiant/dashboard";
            default -> "redirect:/home";
        };
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "dashboard"; // Assurez-vous que ce template existe
    }

    @GetMapping("/enseignant/dashboard")
    public String enseignantDashboard() {
        return "dashboard";
    }
    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode != null && statusCode == 404) {
            return "notFound";
        }
        return "notFound";
    }



    @GetMapping("/etudiant/dashboard")
    public String etudiantDashboard() {
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        // Supprime le refresh token en base
        String refreshToken = extractCookie(request, "REFRESH_TOKEN");
        if (refreshToken != null) {
            try {
                RefreshToken rt = refreshTokenService.verifyExpiration(refreshToken);
                refreshTokenService.deleteByUtilisateur(rt.getUtilisateur());
            } catch (Exception ignored) {}
        }

        // Supprime les deux cookies avec ta méthode déjà définie
        deleteCookie(response, "JWT_TOKEN");
        deleteCookie(response, "REFRESH_TOKEN");

        return "redirect:/login?logout=true";
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