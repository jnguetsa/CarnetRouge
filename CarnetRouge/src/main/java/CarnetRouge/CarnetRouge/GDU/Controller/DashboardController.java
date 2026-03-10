package CarnetRouge.CarnetRouge.GDU.Controller;

import CarnetRouge.CarnetRouge.GDU.DTO.Response.UtilisateursDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Utilisateurs;
import CarnetRouge.CarnetRouge.GDU.Mappers.UtilisateurMapper;
import CarnetRouge.CarnetRouge.GDU.Repository.UtilisateurRepository;
import CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl.AdminServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {
    private final AdminServiceImpl adminService;
    private  final AdminServiceImpl adminServiceImpl;

    public DashboardController(AdminServiceImpl adminService, UtilisateurMapper utilisateursMapper, AdminServiceImpl adminServiceImpl, UtilisateurRepository utilisateurRepository) {
        this.adminService = adminService;
        this.adminServiceImpl = adminServiceImpl;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Utilisateurs utilisateur = (Utilisateurs) authentication.getPrincipal();
        model.addAttribute("user", utilisateur);
        return "dashboard";
    }

    @PostMapping("/admin/utilisateurs/supprimer")
    public String supprimerUtilisateur(@RequestParam Long id) {
        adminServiceImpl.deleteUtilisateur(id);
        return "redirect:/admin/utilisateurs";
    }





    @GetMapping("/admin/utilisateurs")
    public String listerUtilisateurs(
            Model model,
            @RequestParam(defaultValue = "") String recherche,
            @RequestParam(defaultValue = "TOUS") String type, // Nouveau paramètre
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UtilisateursDTO> pageResult = adminService.listeTous(recherche, type, page, size);

        model.addAttribute("utilisateurs", pageResult);
        model.addAttribute("recherche", recherche);
        model.addAttribute("typeSelectionne", type);

        return "utilisateurs";
    }

}

