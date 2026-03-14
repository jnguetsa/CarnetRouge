package CarnetRouge.CarnetRouge.GDU.Controller;

import CarnetRouge.CarnetRouge.GDU.DTO.Response.UtilisateursDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Assistant;
import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import CarnetRouge.CarnetRouge.GDU.Entity.Utilisateurs;
import CarnetRouge.CarnetRouge.GDU.Exception.UserNotFoundException;
import CarnetRouge.CarnetRouge.GDU.Repository.UtilisateurRepository;
import CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final AdminServiceImpl adminService;
    private  final UtilisateurRepository utilisateurRepository;
    // ── Dashboard ──

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Utilisateurs utilisateur = (Utilisateurs) authentication.getPrincipal();
        model.addAttribute("user", utilisateur);
        return "dashboard";
    }

    // ── Liste utilisateurs ──

    @GetMapping("/admin/utilisateurs")
    public String listerUtilisateurs(
            Model model,
            @RequestParam(defaultValue = "") String recherche,
            @RequestParam(defaultValue = "TOUS") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UtilisateursDTO> pageResult = adminService.listeTous(recherche, type, page, size);

        model.addAttribute("utilisateurs", pageResult);
        model.addAttribute("recherche", recherche);
        model.addAttribute("typeSelectionne", type);

        return "utilisateurs";
    }

    @GetMapping("/admin/utilisateurs/details/{id}")
    public String voirDetails(@PathVariable Long id, Model model) {
        Utilisateurs utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        if (utilisateur instanceof Enseignant) {
            model.addAttribute("details", adminService.EnsDetails(id));
            model.addAttribute("type", "ENSEIGNANT");
        } else if (utilisateur instanceof Assistant) {
            model.addAttribute("details", adminService.AssDetails(id));
            model.addAttribute("type", "ASSISTANT");
        } else {
            return "redirect:/admin/utilisateurs?error=TypeInconnu";
        }

        return "details1"; // ← une seule page
    }

/*    @GetMapping("/admin/utilisateurs/details/{id}")
    public String voirDetails(@PathVariable Long id, Model model) {

        Utilisateurs utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        if (utilisateur instanceof Enseignant) {

            model.addAttribute("details", adminService.EnsDetails(id));
            model.addAttribute("type", "ENSEIGNANT");

            return "detailsEnseignant";
        }

        if (utilisateur instanceof Assistant) {

            model.addAttribute("details", adminService.AssDetails(id));
            model.addAttribute("type", "ASSISTANT");

            return "detailsAssistant";
        }

        return "redirect:/admin/utilisateurs?error=TypeInconnu";
    }*/

    @PostMapping("/admin/utilisateurs/supprimer")
    public String supprimerUtilisateur(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes) {
        try {
            adminService.deleteUtilisateur(id);
            redirectAttributes.addFlashAttribute("success", "Utilisateur supprimé avec succès.");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/utilisateurs";}
}