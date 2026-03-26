package CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl;

import CarnetRouge.CarnetRouge.GDET.Entity.Classes;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.*;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.AssistantResponseDetails;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.EnseignantResponseDetails;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.SurveillantResponseDetails;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UtilisateursDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.*;
import CarnetRouge.CarnetRouge.GDU.Exception.*;
import CarnetRouge.CarnetRouge.GDU.Mappers.*;
import CarnetRouge.CarnetRouge.GDU.Repository.*;
import CarnetRouge.CarnetRouge.GDU.Services.InterfaceService.InterfaceServiceAdmin;
import CarnetRouge.CarnetRouge.Notification.Services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements InterfaceServiceAdmin {

    private final EnseignantRepository enseignantRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AssistantRepository assistantRepository;
    private final SurveillantRepository surveillantRepository;
    private final UtilisateurMapper utilisateursMapper;
    private final EnseignantMapper enseignantMapper;
    private final AssistantMappers assistantMappers;
    private final SurveillantMapper surveillantMapper; // ✅ nouveau
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final EmailService emailService;
    private final ClassesRepository classesRepository;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ══════════════════════════════════════════════
    // LISTER
    // ══════════════════════════════════════════════
    @Override
    public Page<UtilisateursDTO> listeTous(String recherche, String type, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        return utilisateurRepository.searchWithFilters(recherche, type, pageable)
                .map(utilisateursMapper::toDTO);
    }

    // ══════════════════════════════════════════════
    // TROUVER PAR ID
    // ══════════════════════════════════════════════
    @Override
    public UtilisateursDTO findById(Long id) {
        Utilisateurs utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé : " + id));
        return utilisateursMapper.toDTO(utilisateur);
    }

    // ══════════════════════════════════════════════
    // SUPPRIMER
    // ══════════════════════════════════════════════
    @Transactional
    @Override
    public void deleteUtilisateur(Long id) {
        if (utilisateurRepository.existsById(id)) {
            utilisateurRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }
    }

    // ══════════════════════════════════════════════
    // ACTIVER / DÉSACTIVER
    // ══════════════════════════════════════════════
    @Transactional
    @Override
    public void activerDesactiverUtilisateur(Long id, boolean activer) {
        Utilisateurs utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + id));
        ActiveUtilisateurRequestDTO requestDTO = new ActiveUtilisateurRequestDTO();
        requestDTO.setActive(activer);
        utilisateursMapper.activateToEntity(requestDTO, utilisateur);
        utilisateurRepository.save(utilisateur);
    }

    // ══════════════════════════════════════════════
    // ENSEIGNANT
    // ══════════════════════════════════════════════
    @Override
    public Enseignant getById(Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new EnseignantNotFoundException(
                        "Enseignant non trouvé avec l'ID : " + id));
    }

    @Transactional
    @Override
    public Enseignant save(EnseignantRequestDTO enseignantRequestDTO) {
        if (enseignantRepository.existsByEmail(enseignantRequestDTO.getEmail())) {
            throw new EmailAlreadyUsedException(
                    "L'email est déjà utilisé : " + enseignantRequestDTO.getEmail());
        }
        Enseignant enseignant = new Enseignant();
        enseignantMapper.ajouterEnseignant(enseignantRequestDTO, enseignant);
        return enseignantRepository.save(enseignant);
    }

    @Transactional
    @Override
    public EnseignantResponseDetails EnsDetails(Long id) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur inexistant avec l'id : " + id));
        return enseignantMapper.toDtoDetails(enseignant);
    }

    // ══════════════════════════════════════════════
    // ASSISTANT
    // ══════════════════════════════════════════════
    @Transactional
    @Override
    public Assistant saveAssistant(AssistantRequestDTO assistantRequestDTO) {
        if (assistantRepository.existsByEmail(assistantRequestDTO.getEmail())) {
            throw new EmailAlreadyUsedException(
                    "L'email est déjà utilisé : " + assistantRequestDTO.getEmail());
        }
        Assistant assistant = new Assistant();
        String encodedPassword = passwordEncoder().encode(assistantRequestDTO.getPassword());
        assistantRequestDTO.setPassword(encodedPassword);
        assistantMappers.ajoutAssistant(assistantRequestDTO, assistant);
        return assistantRepository.save(assistant);
    }

    @Transactional
    @Override
    public AssistantResponseDetails AssDetails(Long id) {
        Assistant assistant = assistantRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur inexistant avec l'id : " + id));
        return assistantMappers.toDtoDetails(assistant);
    }

    // ══════════════════════════════════════════════
    // ✅ SURVEILLANT
    // ══════════════════════════════════════════════
    @Transactional
    @Override
    public SurveillantResponseDetails SurDetails(Long id) {
        Surveillant surveillant = surveillantRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Surveillant inexistant avec l'id : " + id));
        return surveillantMapper.toDtoDetails(surveillant);
    }

    // ══════════════════════════════════════════════
    // RÔLES
    // ══════════════════════════════════════════════
    @Transactional
    @Override
    public ActiveRoleDTORequest activeRole(Long id, ActiveRoleDTORequest activeRoleDTORequest) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleIsNotExisteException("Le rôle n'existe pas"));
        roleMapper.updateRoleFromDTO(activeRoleDTORequest, role);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toActiveRoleDTORequest(savedRole);
    }

    // ══════════════════════════════════════════════
    // PERMISSIONS
    // ══════════════════════════════════════════════
    @Transactional
    @Override
    public ActivePermissionRequest activePermissionRequest(Long id, ActivePermissionRequest activePermissionRequest) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionNotExistException("Cette permission n'existe pas"));
        permissionMapper.updatePermission(activePermissionRequest, permission);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toActivePermmission(savedPermission);
    }

    // ══════════════════════════════════════════════
    // ✅ CRÉER UTILISATEUR
    // ══════════════════════════════════════════════
    @Transactional
    @Override
    public void creerUtilisateur(CreerUtilisateurRequest request) {

        // 1. Vérifier email unique
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsedException(
                    "L'email est déjà utilisé : " + request.getEmail());
        }

        // 2. Récupérer le rôle
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RoleIsNotExisteException(
                        "Rôle introuvable : " + request.getRoleId()));

        // 3. Désactiver les permissions choisies par l'admin
        if (request.getPermissionsDesactivees() != null) {
            role.getPermissions().forEach(p -> {
                if (request.getPermissionsDesactivees().contains(p.getId())) {
                    p.setActive(false);
                }
            });
        }

        // 4. Générer le mot de passe
        String motDePasseBrut   = genererMotDePasse();
        String motDePasseEncode = passwordEncoder().encode(motDePasseBrut);

        // 5. Créer l'utilisateur selon le type
        Utilisateurs utilisateur = switch (request.getTypeUtilisateur()) {

            case "ENS" -> {
                Enseignant ens = new Enseignant();
                ens.setGrade(request.getGrade());
                ens.setTypeEnseignant(request.getTypeEnseignant());
                yield ens;
            }

            case "AST" -> {
                Assistant ast = new Assistant();
                ast.setFonction(request.getFonction());
                if (request.getClassesIds() != null && !request.getClassesIds().isEmpty()) {
                    List<Classes> classes = classesRepository.findAllById(request.getClassesIds());
                    ast.setClasses(classes);
                }
                yield ast;
            }

            case "SUR" -> {
                Surveillant sur = new Surveillant();
                sur.setSecteur(request.getSecteur());
                sur.setTypeContrat(request.getTypeContrat());
                yield sur;
            }

            default -> throw new IllegalArgumentException(
                    "Type utilisateur invalide : " + request.getTypeUtilisateur());
        };

        // 6. Remplir les champs communs
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setDateNaissance(request.getDateNaissance());
        utilisateur.setPassword(motDePasseEncode);
        utilisateur.setActive(true);
        utilisateur.setFirstLogin(true);
        utilisateur.setRoles(new HashSet<>(Set.of(role)));

        // 7. Sauvegarder
        utilisateurRepository.save(utilisateur);

        // 8. Envoyer l'email de bienvenue
        emailService.envoyerEmailBienvenue(
                request.getEmail(),
                request.getPrenom(),
                request.getNom(),
                motDePasseBrut,
                role.getName()
        );
    }

    // ══════════════════════════════════════════════
    // GÉNÉRATION MOT DE PASSE
    // ══════════════════════════════════════════════
    private String genererMotDePasse() {
        String majuscules = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String minuscules = "abcdefghijklmnopqrstuvwxyz";
        String chiffres   = "0123456789";
        String speciaux   = "@#$!%?&";
        String tous       = majuscules + minuscules + chiffres + speciaux;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // Garantir au moins 1 de chaque type
        sb.append(majuscules.charAt(random.nextInt(majuscules.length())));
        sb.append(minuscules.charAt(random.nextInt(minuscules.length())));
        sb.append(chiffres.charAt(random.nextInt(chiffres.length())));
        sb.append(speciaux.charAt(random.nextInt(speciaux.length())));

        // Compléter jusqu'à 10 caractères
        for (int i = 4; i < 10; i++) {
            sb.append(tous.charAt(random.nextInt(tous.length())));
        }

        // Mélanger
        List<Character> chars = new ArrayList<>();
        for (char c : sb.toString().toCharArray()) chars.add(c);
        Collections.shuffle(chars, random);

        StringBuilder resultat = new StringBuilder();
        for (char c : chars) resultat.append(c);
        return resultat.toString();
    }
}