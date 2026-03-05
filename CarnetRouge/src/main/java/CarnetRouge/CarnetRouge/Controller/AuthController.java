package CarnetRouge.CarnetRouge.Controller;

import CarnetRouge.CarnetRouge.Config.JwtService;
import CarnetRouge.CarnetRouge.Entity.Utilisateurs;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
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

            String token = jwtService.generateJwtToken(utilisateur);

            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1 heure
            response.addCookie(cookie);

            return determineTargetUrl(utilisateur.getAuthorities());

        } catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("error", "Identifiants invalides");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue");
            return "redirect:/login";
        }
    }

    /**
     * Détermine l'URL de redirection en fonction des autorités de l'utilisateur
     */
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
            return "404";
        }
        return "404"; // tu peux créer une page 500 séparée plus tard
    }

    

    @GetMapping("/etudiant/dashboard")
    public String etudiantDashboard() {
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Suppression du cookie JWT en mettant son maxAge à 0
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login?logout=true";
    }
}