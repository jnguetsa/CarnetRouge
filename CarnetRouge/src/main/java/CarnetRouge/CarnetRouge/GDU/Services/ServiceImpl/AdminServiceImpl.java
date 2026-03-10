package CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.ActiveUtilisateurRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.AssistantRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Request.EnseignantRequestDTO;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.UtilisateursDTO;
import CarnetRouge.CarnetRouge.GDU.Entity.Assistant;
import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import CarnetRouge.CarnetRouge.GDU.Entity.Utilisateurs;
import CarnetRouge.CarnetRouge.GDU.Exception.EmailAlreadyUsedException;
import CarnetRouge.CarnetRouge.GDU.Exception.EnseignantNotFoundException;
import CarnetRouge.CarnetRouge.GDU.Exception.UserNotFoundException;
import CarnetRouge.CarnetRouge.GDU.Mappers.AssistantMappers;
import CarnetRouge.CarnetRouge.GDU.Mappers.EnseignantMapper;
import CarnetRouge.CarnetRouge.GDU.Mappers.UtilisateurMapper;
import CarnetRouge.CarnetRouge.GDU.Repository.AssistantRepository;
import CarnetRouge.CarnetRouge.GDU.Repository.EnseignantRepository;
import CarnetRouge.CarnetRouge.GDU.Repository.UtilisateurRepository;
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
    private  final UtilisateurMapper utilisateursMapper;
   private  final EnseignantMapper enseignantMapper;
   private  final AssistantMappers  assistantMappers;
    @Override
    @Transactional
    public void activerDesactiverUtilisateur(Long id, boolean activer) {
        Utilisateurs utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'id : " + id));

        ActiveUtilisateurRequestDTO requestDTO = new ActiveUtilisateurRequestDTO();
        requestDTO.setActive(activer);
        utilisateursMapper.activateToEntity(requestDTO, utilisateur);
        utilisateurRepository.save(utilisateur);
    }

    @Override
public Page<UtilisateursDTO> listeTous(String recherche, String type, int page, int size) {
    PageRequest pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
    return utilisateurRepository.searchWithFilters(recherche, type, pageable)
            .map(utilisateursMapper::toDTO);
}
@Transactional
@Override
public void  deleteUtilisateur(Long id) {
    if (utilisateurRepository.existsById(id)) {
        utilisateurRepository.deleteById(id);
    } else {
        throw new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + id);
    }
}

    @Override
    public Enseignant getById(Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new EnseignantNotFoundException(
                        "Enseignant non trouvé avec l'ID : " + id));
    }

    @Transactional
    @Override
    public Enseignant save(EnseignantRequestDTO enseignantRequestDTO) {
        if(enseignantRepository.existsByEmail(enseignantRequestDTO.getEmail())){
            throw new EmailAlreadyUsedException("L'email est déjà utilisé : " + enseignantRequestDTO.getEmail());
        }
        Enseignant enseignant = new Enseignant();
        enseignantMapper.ajouterEnseignant(enseignantRequestDTO, enseignant);

        return enseignantRepository.save(enseignant);
    }



    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Transactional
    @Override
    public Assistant saveAssistant(AssistantRequestDTO assistantRequestDTO) {
        if(assistantRepository.existsByEmail(assistantRequestDTO.getEmail())){
            throw new EmailAlreadyUsedException("L'email est déjà utilisé : " + assistantRequestDTO.getEmail());
        }
        Assistant assistant = new Assistant();
        String rawPassword = assistantRequestDTO.getPassword();
        String encodedPassword = passwordEncoder().encode(rawPassword);
        assistantRequestDTO.setPassword(encodedPassword);
        assistantMappers.ajoutAssistant(assistantRequestDTO, assistant);
        return assistantRepository.save(assistant);
    }
}