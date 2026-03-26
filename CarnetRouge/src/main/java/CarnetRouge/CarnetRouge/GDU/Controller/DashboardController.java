package CarnetRouge.CarnetRouge.GDU.Controller;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.CreerUtilisateurRequest;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UtilisateursDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.*;
import CarnetRouge.CarnetRouge.GDU.Exception.EmailAlreadyUsedException;
import CarnetRouge.CarnetRouge.GDU.Exception.UserNotFoundException;
import CarnetRouge.CarnetRouge.GDU.Repository.ClassesRepository;
import CarnetRouge.CarnetRouge.GDU.Repository.RoleRepository;
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
    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final ClassesRepository classesRepository;

    // ══════════════════════════════════════════════
    // DASHBOARD
    // ══════════════════════════════════════════════
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Utilisateurs utilisateur = (Utilisateurs) authentication.getPrincipal();
        model.addAttribute("user", utilisateur);
        return "dashboard";
    }

    // ══════════════════════════════════════════════
    // LISTE UTILISATEURS
    // ══════════════════════════════════════════════
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

    // ══════════════════════════════════════════════
    // DÉTAILS UTILISATEUR
    // ══════════════════════════════════════════════
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
        } else if (utilisateur instanceof Surveillant) {
            model.addAttribute("details", adminService.SurDetails(id));
            model.addAttribute("type", "SURVEILLANT");
        } else {
            return "redirect:/admin/utilisateurs?error=TypeInconnu";
        }
        return "details1";
    }

    // ══════════════════════════════════════════════
    // ✅ CRÉER UTILISATEUR — Page stepper (GET)
    // ══════════════════════════════════════════════
    @GetMapping("/admin/utilisateurs/createUser")
    public String pageCreerUtilisateur(Model model) {
        // ✅ Rôles actifs pour l'étape 2
        model.addAttribute("roles", roleRepository.findByActive(true));
        // ✅ Toutes les classes pour l'étape 3
        model.addAttribute("classes", classesRepository.findAll());
        return "createUser"; // → templates/createUser.html
    }

    // ══════════════════════════════════════════════
    // ✅ CRÉER UTILISATEUR — Soumission formulaire (POST)
    // ══════════════════════════════════════════════
    @PostMapping("/admin/utilisateurs/createUser")
    public String creerUtilisateur(
            @ModelAttribute CreerUtilisateurRequest request,
            RedirectAttributes redirectAttributes) {
        try {
            adminService.creerUtilisateur(request);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "✅ Utilisateur " + request.getPrenom() + " " + request.getNom()
                            + " créé avec succès. Un email a été envoyé à " + request.getEmail()
            );
            return "redirect:/admin/utilisateurs";

        } catch (EmailAlreadyUsedException e) {
            redirectAttributes.addFlashAttribute("error", "❌ " + e.getMessage());
            return "redirect:/admin/utilisateurs/createUser";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error", "❌ Erreur lors de la création : " + e.getMessage()
            );
            return "redirect:/admin/utilisateurs/createUser";
        }
    }

    // ══════════════════════════════════════════════
    // SUPPRIMER UTILISATEUR
    // ══════════════════════════════════════════════
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
        return "redirect:/admin/utilisateurs";
    }
}