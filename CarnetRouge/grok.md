Voici un exemple de **contrôleur Spring MVC** (avec Thymeleaf) pour gérer les enseignants, en reprenant les méthodes de ton service `EnseignantService` que nous avons corrigées / améliorées précédemment.

Je propose une version classique avec :
- liste paginée
- recherche simple
- formulaire de création / modification
- activation / désactivation
- suppression

```java
package CarnetRouge.CarnetRouge.Controllers;

import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import CarnetRouge.CarnetRouge.Services.EnseignantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enseignants")
@RequiredArgsConstructor
public class EnseignantController {

    private final EnseignantService enseignantService;

    // ─────────────────────────────────────────────
    // LISTE + PAGINATION + RECHERCHE SIMPLE
    // ─────────────────────────────────────────────
    @GetMapping
    public String listeEnseignants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search,
            Model model) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Enseignant> pageEnseignants;

        if (search != null && !search.trim().isEmpty()) {
            pageEnseignants = enseignantService.searchByNomEtPrenom(search.trim(), pageable);
        } else {
            pageEnseignants = enseignantService.findByType(null, pageable); // ou findAll si tu l'ajoutes
        }

        model.addAttribute("enseignants", pageEnseignants.getContent());
        model.addAttribute("page", pageEnseignants);

        // pour garder les filtres dans le formulaire
        model.addAttribute("search", search);
        model.addAttribute("currentSize", size);
        model.addAttribute("currentSort", sortBy);
        model.addAttribute("currentDirection", direction);

        return "enseignants/liste";  // → templates/enseignants/liste.html
    }

    // ─────────────────────────────────────────────
    // FORMULAIRE DE CRÉATION
    // ─────────────────────────────────────────────
    @GetMapping("/nouveau")
    public String nouveauEnseignant(Model model) {
        if (!model.containsAttribute("enseignant")) {
            model.addAttribute("enseignant", new Enseignant());
        }
        model.addAttribute("mode", "create");
        return "enseignants/form";
    }

    // ─────────────────────────────────────────────
    // FORMULAIRE DE MODIFICATION
    // ─────────────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String editerEnseignant(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            Enseignant enseignant = enseignantService.getById(id);
            model.addAttribute("enseignant", enseignant);
            model.addAttribute("mode", "edit");
            return "enseignants/form";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "Enseignant non trouvé");
            return "redirect:/enseignants";
        }
    }

    // ─────────────────────────────────────────────
    // SAUVEGARDE (CREATE + UPDATE)
    // ─────────────────────────────────────────────
    @PostMapping("/save")
    public String sauvegarderEnseignant(
            @Valid @ModelAttribute("enseignant") Enseignant enseignant,
            BindingResult bindingResult,
            RedirectAttributes redirect,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", enseignant.getId() == null ? "create" : "edit");
            return "enseignants/form";
        }

        try {
            enseignantService.save(enseignant);
            redirect.addFlashAttribute("successMessage",
                    enseignant.getId() == null ? "Enseignant créé avec succès" : "Enseignant modifié avec succès");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("mode", enseignant.getId() == null ? "create" : "edit");
            return "enseignants/form";
        }

        return "redirect:/enseignants";
    }

    // ─────────────────────────────────────────────
    // ACTIVATION / DÉSACTIVATION
    // ─────────────────────────────────────────────
    @PostMapping("/{id}/activer")
    public String activer(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            enseignantService.activer(id);
            redirect.addFlashAttribute("successMessage", "Enseignant activé");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/enseignants";
    }

    @PostMapping("/{id}/desactiver")
    public String desactiver(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            enseignantService.desactiver(id);
            redirect.addFlashAttribute("successMessage", "Enseignant désactivé");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/enseignants";
    }

    // ─────────────────────────────────────────────
    // SUPPRESSION
    // ─────────────────────────────────────────────
    @PostMapping("/{id}/delete")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            enseignantService.deleteById(id);
            redirect.addFlashAttribute("successMessage", "Enseignant supprimé avec succès");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/enseignants";
    }
}
```

### Quelques templates Thymeleaf associés (très simplifiés)

#### `liste.html` (extrait)

```html
<table class="table">
  <thead>
    <tr>
      <th>Nom</th>
      <th>Prénom</th>
      <th>Email</th>
      <th>Statut</th>
      <th>Actions</th>
    </tr>
  </thead>
  <tbody>
    <tr th:each="ens : ${enseignants}">
      <td th:text="${ens.nom}"></td>
      <td th:text="${ens.prenom}"></td>
      <td th:text="${ens.email}"></td>
      <td>
        <span th:if="${ens.active}" class="badge bg-success">Actif</span>
        <span th:unless="${ens.active}" class="badge bg-danger">Inactif</span>
      </td>
      <td>
        <a th:href="@{/enseignants/edit/{id}(id=${ens.id})}" class="btn btn-sm btn-primary">Modifier</a>
        
        <form th:action="@{/enseignants/{id}/activer(id=${ens.id})}" method="post" style="display:inline;" 
              th:if="${!ens.active}">
          <button type="submit" class="btn btn-sm btn-success">Activer</button>
        </form>
        
        <form th:action="@{/enseignants/{id}/desactiver(id=${ens.id})}" method="post" style="display:inline;" 
              th:if="${ens.active}">
          <button type="submit" class="btn btn-sm btn-warning">Désactiver</button>
        </form>
        
        <form th:action="@{/enseignants/{id}/delete(id=${ens.id})}" method="post" style="display:inline;"
              onclick="return confirm('Confirmer la suppression ?');">
          <button type="submit" class="btn btn-sm btn-danger">Supprimer</button>
        </form>
      </td>
    </tr>
  </tbody>
</table>

<!-- Pagination -->
<div th:if="${page.totalPages > 1}">
  <!-- liens page précédente / suivante + numéros -->
</div>

<!-- formulaire de recherche -->
<form th:action="@{/enseignants}" method="get">
  <input type="text" name="search" th:value="${search}" placeholder="Nom ou prénom..."/>
  <button type="submit">Rechercher</button>
</form>
```

Est-ce que tu veux :
- une version avec plus de champs dans le formulaire ?
- gestion des rôles / typeEnseignant dans le form ?
- ajout de validation côté Thymeleaf ?
- gestion des messages d’erreur plus élaborée ?

Dis-moi ce que tu veux approfondir ou modifier !