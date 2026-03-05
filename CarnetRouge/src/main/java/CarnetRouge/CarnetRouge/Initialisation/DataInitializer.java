package CarnetRouge.CarnetRouge.Initialisation;

import CarnetRouge.CarnetRouge.Entity.*;
import CarnetRouge.CarnetRouge.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UtilisateurRepository utilisateursRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // 1. PERMISSIONS (On récupère l'objet directement)
        Permission pRead = createOrUpdatePermission("enseignant:read", "Voir les enseignants");
        Permission pWrite = createOrUpdatePermission("enseignant:write", "Modifier les enseignants");
        Permission pDelete = createOrUpdatePermission("enseignant:delete", "Supprimer les enseignants");
        Permission eRead = createOrUpdatePermission("etudiant:read", "Voir les étudiants");
        Permission eWrite = createOrUpdatePermission("etudiant:write", "Modifier les étudiants");
        Permission eDelete = createOrUpdatePermission("etudiant:delete", "Supprimer les étudiants");
        Permission nRead = createOrUpdatePermission("note:read", "Voir les notes");
        Permission nWrite = createOrUpdatePermission("note:write", "Saisir les notes");

        // 2. ROLES (On utilise les objets récupérés, et on enlève "ROLE_" du nom en DB)
        Role roleAdmin = createOrUpdateRole("ADMIN", "Administrateur", true,
                Set.of(pRead, pWrite, pDelete, eRead, eWrite, eDelete, nRead, nWrite));

        Role roleEnseignant = createOrUpdateRole("ENSEIGNANT", "Enseignant", true,
                Set.of(eRead, nRead, nWrite));

        Role roleEtudiant = createOrUpdateRole("ETUDIANT", "Étudiant", true,
                Set.of(nRead));

        // 3. UTILISATEURS
        createAdmin(roleAdmin);
        createEnseignant(roleEnseignant);
        createEtudiant(roleEtudiant);

        System.out.println("✅ Initialisation terminée sans erreur !");
    }

    // --- HELPERS AMÉLIORÉS ---

    private Permission createOrUpdatePermission(String name, String description) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(
                        Permission.builder()
                                .name(name)
                                .description(description)
                                .active(true)
                                .creatAt(LocalDateTime.now())
                                .build()
                ));
    }

    private Role createOrUpdateRole(String name, String description, boolean active, Set<Permission> permissions) {
        return roleRepository.findByName(name)
                .map(role -> {
                    role.setPermissions(new HashSet<>(permissions));
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
        if (utilisateursRepository.findByEmail("admin@carnetrouge.com").isEmpty()) {
            Administrateur admin = Administrateur.builder()
                    .nom("Dupont").prenom("Jean").email("admin@carnetrouge.com")
                    .password(passwordEncoder.encode("Admin123!"))
                    .telephone("0600000001").active(true).locked(false).expired(false)
                    .createdAt(LocalDateTime.now()).roles(new HashSet<>(Set.of(role)))
                    .build();
            utilisateursRepository.save(admin);
        }
    }

    private void createEnseignant(Role role) {
        if (utilisateursRepository.findByEmail("enseignant@carnetrouge.com").isEmpty()) {
            Enseignant ens = Enseignant.builder()
                    .nom("Martin").prenom("Sophie").email("enseignant@carnetrouge.com")
                    .password(passwordEncoder.encode("Ens123!"))
                    .telephone("0600000002").active(true).locked(false).expired(false)
                    .grade("Maître de conférences").typeEnseignant("Permanent")
                    .createdAt(LocalDateTime.now()).roles(new HashSet<>(Set.of(role)))
                    .build();
            utilisateursRepository.save(ens);
        }
    }

    private void createEtudiant(Role role) {
        if (utilisateursRepository.findByEmail("etudiant@carnetrouge.com").isEmpty()) {
            Etudiant etu = Etudiant.builder()
                    .nom("Bernard").prenom("Lucas").email("etudiant@carnetrouge.com")
                    .password(passwordEncoder.encode("Etu123!"))
                    .telephone("0600000003").active(true).locked(false).expired(false)
                    .createdAt(LocalDateTime.now()).roles(new HashSet<>(Set.of(role)))
                    .build();
            utilisateursRepository.save(etu);
        }
    }
}
