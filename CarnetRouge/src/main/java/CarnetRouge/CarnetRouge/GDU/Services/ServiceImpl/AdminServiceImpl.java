package CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.*;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.AssistantResponseDetails;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.EnseignantResponseDetails;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UtilisateursDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.*;
import CarnetRouge.CarnetRouge.GDU.Exception.*;
import CarnetRouge.CarnetRouge.GDU.Mappers.*;
import CarnetRouge.CarnetRouge.GDU.Repository.*;
import CarnetRouge.CarnetRouge.GDU.Services.InterfaceService.InterfaceServiceAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements InterfaceServiceAdmin {

    private final EnseignantRepository enseignantRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AssistantRepository assistantRepository;
    private final UtilisateurMapper utilisateursMapper;
    private final EnseignantMapper enseignantMapper;
    private final AssistantMappers assistantMappers;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
   private  final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ── Lister ──

    @Override
    public Page<UtilisateursDTO> listeTous(String recherche, String type, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        return utilisateurRepository.searchWithFilters(recherche, type, pageable)
                .map(utilisateursMapper::toDTO);
    }

    // ── Trouver par ID ──

    @Override
    public UtilisateursDTO findById(Long id) {
        Utilisateurs utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé : " + id));
        return utilisateursMapper.toDTO(utilisateur);
    }

    // ── Modifier ──




    // ── Supprimer ──

    @Transactional
    @Override
    public void deleteUtilisateur(Long id) {
        if (utilisateurRepository.existsById(id)) {
            utilisateurRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }
    }

    // ── Activer / Désactiver ──

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

    // ── Enseignant ──

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

    // ── Assistant ──

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
    public EnseignantResponseDetails EnsDetails(Long id) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur inexistant avec l'id : " + id));
        return enseignantMapper.toDtoDetails(enseignant);
    }
    @Transactional
    @Override
    public AssistantResponseDetails AssDetails(Long id){
        Assistant assistant= assistantRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("Utilisateur inexistant avec l'id : " + id));

        return assistantMappers.toDtoDetails(assistant);
   }

/*
 Methode de desacitation d'un role
 */
    @Transactional
    @Override
    public ActiveRoleDTORequest activeRole(Long id, ActiveRoleDTORequest activeRoleDTORequest) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleIsNotExisteException("Le rôle n'existe pas"));

        roleMapper.updateRoleFromDTO(activeRoleDTORequest, role);

        Role savedRole = roleRepository.save(role);

        return roleMapper.toActiveRoleDTORequest(savedRole);
    }

    /*
    methode d'activation et desactivation d'une permission
     */
    @Transactional
    @Override
    public ActivePermissionRequest activePermissionRequest(Long id, ActivePermissionRequest activePermissionRequest){
        Permission permission= permissionRepository.findById(id)
                .orElseThrow(()-> new PermissionNotExistException(" Cette permmissions n'existe pas"));
        permissionMapper.updatePermission(activePermissionRequest, permission);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toActivePermmission(savedPermission);
    }


}