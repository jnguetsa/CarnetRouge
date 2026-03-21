package CarnetRouge.CarnetRouge.GDU.Initialisation;

import CarnetRouge.CarnetRouge.GDU.Entity.*;
import CarnetRouge.CarnetRouge.GDU.Repository.PermissionRepository;
import CarnetRouge.CarnetRouge.GDU.Repository.RoleRepository;
import CarnetRouge.CarnetRouge.GDU.Repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Order(1) // ✅ S'exécute EN PREMIER — crée les utilisateurs avant DataInitializer1
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // 1. PERMISSIONS
        Permission pRead   = createOrUpdatePermission("enseignant:read",   "Voir les enseignants");
        Permission pWrite  = createOrUpdatePermission("enseignant:write",  "Modifier les enseignants");
        Permission pDelete = createOrUpdatePermission("enseignant:delete", "Supprimer les enseignants");

        Permission eRead   = createOrUpdatePermission("etudiant:read",   "Voir les étudiants");
        Permission eWrite  = createOrUpdatePermission("etudiant:write",  "Modifier les étudiants");
        Permission eDelete = createOrUpdatePermission("etudiant:delete", "Supprimer les étudiants");

        Permission nRead  = createOrUpdatePermission("note:read",  "Voir les notes");
        Permission nWrite = createOrUpdatePermission("note:write", "Saisir / modifier les notes");

        // 2. ROLES
        Role roleAdmin      = createOrUpdateRole("ADMIN",      "Administrateur",                        true, Set.of(pRead, pWrite, pDelete, eRead, eWrite, eDelete, nRead, nWrite));
        Role roleEnseignant = createOrUpdateRole("ENSEIGNANT", "Enseignant",                            true, Set.of(eRead, nRead, nWrite));
        Role roleEtudiant   = createOrUpdateRole("ETUDIANT",   "Étudiant",                              true, Set.of(nRead));
        Role roleAssistant  = createOrUpdateRole("ASSISTANT",  "Assistant administratif / pédagogique", true, Set.of(eRead, nRead));

        // 3. UTILISATEURS de base
        createAdmin(roleAdmin);
        createEnseignant(roleEnseignant);
        createEtudiant(roleEtudiant);

        // 4. Enseignants et assistants fictifs (données de test)
        createFakeUsers(roleEnseignant, roleAssistant);

        System.out.println("✅ DataInitializer — permissions, rôles, utilisateurs créés");
    }

    // ────────────────────────────────────────────────
    // HELPERS
    // ────────────────────────────────────────────────

    private Permission createOrUpdatePermission(String name, String description) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(
                        Permission.builder()
                                .name(name)
                                .description(description)
                                .active(true)
                                .creatAt(LocalDateTime.now()) // ✅ corrigé — un seul creatAt
                                .build()
                ));
    }

    private Role createOrUpdateRole(String name, String description, boolean active, Set<Permission> permissions) {
        return roleRepository.findByName(name)
                .map(role -> {
                    role.setPermissions(new HashSet<>(permissions));
                    role.setActive(active);
                    return roleRepository.save(role);
                })
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name(name)
                                .description(description)
                                .active(active)
                                .creatAt(LocalDateTime.now())
                                .permissions(new HashSet<>(permissions))
                                .build()
                ));
    }

    private void createAdmin(Role role) {
        if (utilisateurRepository.findByEmail("admin@carnetrouge.com").isEmpty()) {
            utilisateurRepository.save(
                    Administrateur.builder()
                            .nom("Dupont")
                            .prenom("Jean")
                            .email("admin@carnetrouge.com")
                            .password(passwordEncoder.encode("Admin123!"))
                            .telephone("0600000001")
                            .dateNaissance(LocalDate.of(1990, 9, 17))
                            .active(true)
                            .createdAt(LocalDateTime.now())
                            .roles(new HashSet<>(Set.of(role)))
                            .build()
            );
        }
    }

    private void createEnseignant(Role role) {
        if (utilisateurRepository.findByEmail("enseignant@carnetrouge.com").isEmpty()) {
            utilisateurRepository.save(
                    Enseignant.builder()
                            .nom("Martin")
                            .prenom("Sophie")
                            .email("enseignant@carnetrouge.com")
                            .password(passwordEncoder.encode("Ens123!"))
                            .telephone("0600000002")
                            .dateNaissance(LocalDate.of(1988, 9, 17))
                            .active(true)
                            .grade("Maître de conférences")
                            .typeEnseignant("Permanent")
                            .createdAt(LocalDateTime.now())
                            .roles(new HashSet<>(Set.of(role)))
                            .build()
            );
        }
    }

    private void createEtudiant(Role role) {
        if (utilisateurRepository.findByEmail("etudiant@carnetrouge.com").isEmpty()) {
            utilisateurRepository.save(
                    Etudiant.builder()
                            .nom("Bernard")
                            .prenom("Lucas")
                            .email("etudiant@carnetrouge.com")
                            .password(passwordEncoder.encode("Etu123!"))
                            .telephone("0600000003")
                            .dateNaissance(LocalDate.of(2003, 11, 28))
                            .active(true)
                            .createdAt(LocalDateTime.now())
                            .roles(new HashSet<>(Set.of(role)))
                            .build()
            );
        }
    }

    private void createFakeUsers(Role roleEnseignant, Role roleAssistant) {

        // ✅ Enseignants fictifs — ceux utilisés dans DataInitializer1
        record EnseignantData(String nom, String prenom, String email, String grade, String type) {}
        List<EnseignantData> enseignants = List.of(
                new EnseignantData("Kamga",  "Paul",   "kamga@carnetrouge.com",   "Professeur",            "Permanent"),
                new EnseignantData("Nkomo",  "Alice",  "nkomo@carnetrouge.com",   "Maître de conférences", "Vacataire"),
                new EnseignantData("Biya",   "Marc",   "biya@carnetrouge.com",    "Docteur",               "Permanent"),
                new EnseignantData("Fouda",  "Claire", "fouda@carnetrouge.com",   "Maître assistant",      "Permanent"),
                new EnseignantData("Mbarga", "Samuel", "mbarga@carnetrouge.com",  "Professeur agrégé",     "Vacataire"),
                // ✅ Enseignants utilisés dans DataInitializer1 pour les UEs
                new EnseignantData("Billong", "Jean",  "billong@carnetrouge.com", "Docteur",               "Permanent"),
                new EnseignantData("Pessa",   "Marie", "pessa@carnetrouge.com",   "Maître assistant",      "Permanent"),
                new EnseignantData("Mballa",  "Paul",  "mballa@carnetrouge.com",  "Professeur",            "Permanent"),
                new EnseignantData("Nitcheu", "Alice", "nitcheu@carnetrouge.com", "Maître de conférences", "Vacataire"),
                new EnseignantData("Mboa",    "Jean",  "mboa@carnetrouge.com",    "Docteur",               "Permanent"),
                new EnseignantData("Biamou",  "Marc",  "biamou@carnetrouge.com",  "Maître assistant",      "Permanent")
        );

        record AssistantData(String nom, String prenom, String email, String fonction) {}
        List<AssistantData> assistants = List.of(
                new AssistantData("Essama", "Julie",  "essama@carnetrouge.com", "Assistante administrative"),
                new AssistantData("Ateba",  "Boris",  "ateba@carnetrouge.com",  "Assistant pédagogique"),
                new AssistantData("Ondoa",  "Grace",  "ondoa@carnetrouge.com",  "Assistante de direction"),
                new AssistantData("Mbassi", "Kevin",  "mbassi@carnetrouge.com", "Assistant informatique"),
                new AssistantData("Zang",   "Nadine", "zang@carnetrouge.com",   "Assistante RH")
        );

        enseignants.forEach(data -> {
            if (utilisateurRepository.findByEmail(data.email()).isEmpty()) {
                String tel = "06000000" + (10 + (int)(Math.random() * 90));
                utilisateurRepository.save(
                        Enseignant.builder()
                                .nom(data.nom())
                                .prenom(data.prenom())
                                .email(data.email())
                                .password(passwordEncoder.encode("Ens123!"))
                                .telephone(tel)
                                .dateNaissance(LocalDate.of(2000, 9, 17))
                                .active(true)
                                .grade(data.grade())
                                .typeEnseignant(data.type())
                                .createdAt(LocalDateTime.now())
                                .roles(new HashSet<>(Set.of(roleEnseignant)))
                                .build()
                );
            }
        });

        assistants.forEach(data -> {
            if (utilisateurRepository.findByEmail(data.email()).isEmpty()) {
                String tel = "06000000" + (10 + (int)(Math.random() * 90));
                utilisateurRepository.save(
                        Assistant.builder()
                                .nom(data.nom())
                                .prenom(data.prenom())
                                .email(data.email())
                                .password(passwordEncoder.encode("Ass123!"))
                                .telephone(tel)
                                .dateNaissance(LocalDate.of(1988, 9, 17))
                                .active(true)
                                .fonction(data.fonction())
                                .createdAt(LocalDateTime.now())
                                .roles(new HashSet<>(Set.of(roleAssistant)))
                                .build()
                );
            }
        });
    }
}