package CarnetRouge.CarnetRouge.Controller;

import CarnetRouge.CarnetRouge.Entity.Utilisateurs;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ✅ Correct
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Utilisateurs utilisateur = (Utilisateurs) authentication.getPrincipal();
        model.addAttribute("user", utilisateur);
        return "dashboard";
    }
}