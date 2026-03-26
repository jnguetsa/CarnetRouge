package CarnetRouge.CarnetRouge.GDU.Services.ServiceImpl;

import CarnetRouge.CarnetRouge.GDU.DTO.Request.PlageHoraireRequest;
import CarnetRouge.CarnetRouge.GDU.DTO.Response.PlageHoraireResponse;
import CarnetRouge.CarnetRouge.GDET.Entity.Classes;
import CarnetRouge.CarnetRouge.GDU.Entity.Enseignant;
import CarnetRouge.CarnetRouge.GDET.Entity.PlageHoraire;
import CarnetRouge.CarnetRouge.GDAE.Entity.UE;
import CarnetRouge.CarnetRouge.GDU.Exception.UserNotFoundException;
import CarnetRouge.CarnetRouge.GDU.Mappers.PlageHoraireMapper;
import CarnetRouge.CarnetRouge.GDU.Repository.ClassesRepository;
import CarnetRouge.CarnetRouge.GDU.Repository.PlageHoraireRepository;
import CarnetRouge.CarnetRouge.GDU.Repository.UERepository;
import CarnetRouge.CarnetRouge.GDU.Repository.UtilisateurRepository;
import CarnetRouge.CarnetRouge.GDU.Services.InterfaceService.PlageHoraireService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// ✅ Injecter le mapper dans le service
@Service
@RequiredArgsConstructor
public class PlageHoraireServiceImpl implements PlageHoraireService {

    private final PlageHoraireRepository plageHoraireRepository;
    private final UERepository ueRepository;
    private final ClassesRepository classesRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PlageHoraireMapper plageHoraireMapper; // ✅ MapStruct

    // ── CREER ──
    @Override
    @Transactional
    public PlageHoraireResponse creer(PlageHoraireRequest request) {

        // 1. Vérifier que les entités existent
        UE ue = ueRepository.findById(request.getUeId())
                .orElseThrow(() -> new UserNotFoundException("UE introuvable"));

        Classes classe = classesRepository.findById(request.getClasseId())
                .orElseThrow(() -> new UserNotFoundException("Classe introuvable"));

        Enseignant enseignant = utilisateurRepository.findById(request.getEnseignantId())
                .filter(u -> u instanceof Enseignant)
                .map(u -> (Enseignant) u)
                .orElseThrow(() -> new UserNotFoundException("Enseignant introuvable"));

        // 2. Vérifier les conflits
        if (plageHoraireRepository.existsConflitEnseignant(
                request.getEnseignantId(),
                request.getJour(),
                request.getHeureDebut(),
                request.getHeureFin())) {
            throw new RuntimeException(
                    "Conflit : " + enseignant.getPrenom() + " " + enseignant.getNom()
                            + " est déjà occupé sur ce créneau"
            );
        }

        if (plageHoraireRepository.existsConflitClasse(
                request.getClasseId(),
                request.getJour(),
                request.getHeureDebut(),
                request.getHeureFin())) {
            throw new RuntimeException(
                    "Conflit : la classe " + classe.getNom()
                            + " a déjà un cours sur ce créneau"
            );
        }

        // 3. Créer la séance
        PlageHoraire plage = PlageHoraire.builder()
                .jour(request.getJour())
                .heureDebut(request.getHeureDebut())
                .heureFin(request.getHeureFin())
                .salle(request.getSalle())
                .couleur(request.getCouleur() != null ? request.getCouleur() : "#dc2626")
                .ue(ue)
                .classe(classe)
                .enseignant(enseignant)
                .build();

        // 4. Sauvegarder et mapper avec MapStruct ✅
        return plageHoraireMapper.toResponse(plageHoraireRepository.save(plage));
    }


    // ── MODIFIER ──
    @Override
    @Transactional
    public PlageHoraireResponse modifier(Long id, PlageHoraireRequest request) {

        // 1. Récupérer la séance existante
        PlageHoraire plage = plageHoraireRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Séance introuvable"));

        // 2. Vérifier que les entités existent
        UE ue = ueRepository.findById(request.getUeId())
                .orElseThrow(() -> new UserNotFoundException("UE introuvable"));

        Classes classe = classesRepository.findById(request.getClasseId())
                .orElseThrow(() -> new UserNotFoundException("Classe introuvable"));

        Enseignant enseignant = utilisateurRepository.findById(request.getEnseignantId())
                .filter(u -> u instanceof Enseignant)
                .map(u -> (Enseignant) u)
                .orElseThrow(() -> new UserNotFoundException("Enseignant introuvable"));

        // 3. Vérifier les conflits en excluant la séance actuelle
        if (plageHoraireRepository.existsConflitEnseignantSaufId(
                request.getEnseignantId(),
                request.getJour(),
                request.getHeureDebut(),
                request.getHeureFin(),
                id)) {
            throw new RuntimeException(
                    "Conflit : " + enseignant.getPrenom() + " " + enseignant.getNom()
                            + " est déjà occupé sur ce créneau"
            );
        }

        if (plageHoraireRepository.existsConflitClasseSaufId(
                request.getClasseId(),
                request.getJour(),
                request.getHeureDebut(),
                request.getHeureFin(),
                id)) {
            throw new RuntimeException(
                    "Conflit : la classe " + classe.getNom()
                            + " a déjà un cours sur ce créneau"
            );
        }

        // 4. Mettre à jour
        plage.setJour(request.getJour());
        plage.setHeureDebut(request.getHeureDebut());
        plage.setHeureFin(request.getHeureFin());
        plage.setSalle(request.getSalle());
        plage.setCouleur(request.getCouleur() != null ? request.getCouleur() : "#dc2626");
        plage.setUe(ue);
        plage.setClasse(classe);
        plage.setEnseignant(enseignant);

        // 5. Sauvegarder et mapper ✅
        return plageHoraireMapper.toResponse(plageHoraireRepository.save(plage));
    }


    // ── SUPPRIMER ──
    @Override
    @Transactional
    public void supprimer(Long id) {
        if (!plageHoraireRepository.existsById(id)) {
            throw new UserNotFoundException("Séance introuvable");
        }
        plageHoraireRepository.deleteById(id);
    }

    @Override
    public PlageHoraireResponse findById(Long id) {
        return plageHoraireRepository.findById(id)
                .map(plageHoraireMapper::toResponse) // ✅ au lieu de toResponse()
                .orElseThrow(() -> new UserNotFoundException("Séance introuvable"));
    }

    @Override
    public List<PlageHoraireResponse> getEdtClasse(Long classeId) {
        return plageHoraireMapper.toResponseList( // ✅ au lieu du stream
                plageHoraireRepository.findByClasseId(classeId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlageHoraireResponse> getEdtClasseSemaine(Long classeId, LocalDate debut, LocalDate fin) {
        return plageHoraireMapper.toResponseList(
                plageHoraireRepository.findByClasseIdAndJourBetween(classeId, debut, fin)
        );
    }

    // ── EDT ENSEIGNANT ──
    @Override
    @Transactional(readOnly = true)
    public List<PlageHoraireResponse> getEdtEnseignant(Long enseignantId) {
        return plageHoraireMapper.toResponseList(
                plageHoraireRepository.findByEnseignantId(enseignantId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlageHoraireResponse> getEdtEnseignantSemaine(Long enseignantId, LocalDate debut, LocalDate fin) {
        return plageHoraireMapper.toResponseList(
                plageHoraireRepository.findByEnseignantIdAndJourBetween(enseignantId, debut, fin)
        );
    }

    // ── POUR FULLCALENDAR ──
    @Override
    @Transactional(readOnly = true)
    public List<PlageHoraireResponse> getEdtClassePourCalendrier(Long classeId, LocalDate debut, LocalDate fin) {
        return plageHoraireMapper.toResponseList(
                plageHoraireRepository.findByClasseIdAndJourBetween(classeId, debut, fin)
        );
    }



    // ... même principe pour toutes les autres méthodes
}