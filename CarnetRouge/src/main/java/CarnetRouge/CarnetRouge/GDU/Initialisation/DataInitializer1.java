package CarnetRouge.CarnetRouge.GDU.Initialisation;

import CarnetRouge.CarnetRouge.GDU.Entity.*;
import CarnetRouge.CarnetRouge.GDU.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@Order(2) // ✅ S'exécute APRÈS DataInitializer — les enseignants existent déjà
@RequiredArgsConstructor
public class DataInitializer1 implements ApplicationRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final SpecialiteRepository specialiteRepository;
    private final ClassesRepository classesRepository;
    private final UERepository ueRepository;
    private final PlageHoraireRepository plageHoraireRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createSpecialitesEtClasses();
        createUEs();
        createPlagesHoraires();
        System.out.println("✅ DataInitializer1 — spécialités, classes, UEs, EDT créés");
    }

    // ─────────────────────────────────────────
    // SPÉCIALITÉS ET CLASSES
    // ─────────────────────────────────────────
    private void createSpecialitesEtClasses() {

        Specialite gInfo  = createOrUpdateSpecialite("Génie Informatique",  "GI", "Spécialité en développement logiciel et systèmes");
        Specialite gLog   = createOrUpdateSpecialite("Génie Logiciel",      "GL", "Spécialité en conception et architecture logicielle");
        Specialite reseau = createOrUpdateSpecialite("Réseaux et Télécoms", "RT", "Spécialité en infrastructure réseau et télécommunications");

        // Classes GI
        createOrUpdateClasse("L1 GI", gInfo);
        createOrUpdateClasse("L2 GI", gInfo);
        createOrUpdateClasse("L3 GI", gInfo);
        createOrUpdateClasse("M1 GI", gInfo);
        createOrUpdateClasse("M2 GI", gInfo);

        // Classes GL
        createOrUpdateClasse("L1 GL", gLog);
        createOrUpdateClasse("L2 GL", gLog);
        createOrUpdateClasse("L3 GL", gLog);
        createOrUpdateClasse("M1 GL", gLog);

        // Classes RT
        createOrUpdateClasse("L1 RT", reseau);
        createOrUpdateClasse("L2 RT", reseau);
        createOrUpdateClasse("L3 RT", reseau);

        System.out.println("✅ Spécialités et classes créées");
    }

    // ─────────────────────────────────────────
    // UE (COURS) + LIAISON ENSEIGNANTS
    // ─────────────────────────────────────────
    private void createUEs() {

        // ✅ Récupérer les enseignants créés par DataInitializer
        Enseignant billong = getEnseignant("billong@carnetrouge.com");
        Enseignant pessa   = getEnseignant("pessa@carnetrouge.com");
        Enseignant mballa  = getEnseignant("mballa@carnetrouge.com");
        Enseignant nitcheu = getEnseignant("nitcheu@carnetrouge.com");
        Enseignant mboa    = getEnseignant("mboa@carnetrouge.com");
        Enseignant biamou  = getEnseignant("biamou@carnetrouge.com");
        Enseignant kamga   = getEnseignant("kamga@carnetrouge.com");

        // ✅ Création des UE
        UE algo   = createOrUpdateUE("Algorithmique",         "INF101", "Introduction aux algorithmes",     3L, 45L);
        UE bdd    = createOrUpdateUE("Base de données",        "INF201", "Conception et requêtes SQL",        3L, 45L);
        UE web    = createOrUpdateUE("Programmation Web Java", "INF301", "Spring Boot et développement web",  4L, 60L);
        UE reseau = createOrUpdateUE("Réseaux",                "INF401", "Protocoles et architecture réseau", 3L, 45L);
        UE ihm    = createOrUpdateUE("IHM",                    "INF701", "Interfaces homme-machine",          2L, 30L);
        UE comm   = createOrUpdateUE("Communication",          "GEN101", "Techniques de communication",       2L, 30L);
        UE sport  = createOrUpdateUE("Sport",                  "SPO101", "Activités physiques et sportives",  1L, 30L);
        UE maths  = createOrUpdateUE("Mathématiques",          "MAT101", "Algèbre et analyse",                4L, 60L);

        // ✅ Liaisons UE ↔ Enseignants
        // Algorithmique → Billong + Mballa
        addEnseignantToUE(algo, billong);
        addEnseignantToUE(algo, mballa);
        // BDD → Pessa
        addEnseignantToUE(bdd, pessa);
        // Web Java → Billong
        addEnseignantToUE(web, billong);
        // Réseaux → Mballa
        addEnseignantToUE(reseau, mballa);
        // IHM → Nitcheu
        addEnseignantToUE(ihm, nitcheu);
        // Communication → Mboa
        addEnseignantToUE(comm, mboa);
        // Sport → Biamou
        addEnseignantToUE(sport, biamou);
        // Maths → Kamga
        addEnseignantToUE(maths, kamga);

        Classes l1GI = classesRepository.findByNom("L1 GI").orElse(null);
        Classes l2GI = classesRepository.findByNom("L2 GI").orElse(null);
        Classes l3GI = classesRepository.findByNom("L3 GI").orElse(null);
        Classes l1GL = classesRepository.findByNom("L1 GL").orElse(null);
        Classes l2GL = classesRepository.findByNom("L2 GL").orElse(null);
        Classes l3GL = classesRepository.findByNom("L3 GL").orElse(null);

        // ✅ Lier les classes aux UEs
        // L3 GI suit : Algo, BDD, Web, Réseaux, IHM, Comm, Sport
        addUEToClasse(l3GI, algo, bdd, web, reseau, ihm, comm, sport);

        // L2 GI suit : Algo, Maths, Comm, Sport
        addUEToClasse(l2GI, algo, maths, comm, sport);

        // L1 GI suit : Algo, Maths, Comm, Sport
        addUEToClasse(l1GI, algo, maths, comm, sport);

        // L3 GL suit : Web, BDD, IHM, Comm, Sport
        addUEToClasse(l3GL, web, bdd, ihm, comm, sport);

        // L2 GL suit : Algo, Maths, BDD
        addUEToClasse(l2GL, algo, maths, bdd);

        // L1 GL suit : Algo, Maths
        addUEToClasse(l1GL, algo, maths);

        // ✅ Sauvegarder les classes (côté propriétaire de classes_ue)
        classesRepository.saveAll(List.of(l1GI, l2GI, l3GI, l1GL, l2GL, l3GL)
                .stream().filter(c -> c != null).toList());

        // ✅ Sauvegarder les UEs (pour les liaisons enseignants)
        ueRepository.saveAll(List.of(algo, bdd, web, reseau, ihm, comm, sport, maths));

        System.out.println("✅ UEs + enseignants + classes liés");
    }

    private void addUEToClasse(Classes classe, UE... ues) {
        if (classe == null) return;
        for (UE ue : ues) {
            if (ue == null) continue;
            boolean dejaLie = classe.getUe().stream()
                    .anyMatch(u -> u.getId().equals(ue.getId()));
            if (!dejaLie) {
                classe.getUe().add(ue);
            }
        }
    }

    // ✅ Ajoute un enseignant à une UE en évitant les doublons
    // ⚠️ Ne pas appeler enseignant.getUes().add(ue) — côté mappedBy, ne persiste pas
    private void addEnseignantToUE(UE ue, Enseignant enseignant) {
        if (ue == null || enseignant == null) return;
        boolean dejaLie = ue.getEnseignants().stream()
                .anyMatch(e -> e.getId().equals(enseignant.getId()));
        if (!dejaLie) {
            ue.getEnseignants().add(enseignant);
        }
    }

    // ─────────────────────────────────────────
    // PLAGES HORAIRES (EDT de la semaine)
    // ─────────────────────────────────────────
    private void createPlagesHoraires() {

        Classes l3GI = classesRepository.findByNom("L3 GI").orElse(null);
        Classes l2GI = classesRepository.findByNom("L2 GI").orElse(null);

        if (l3GI == null || l2GI == null) {
            System.out.println("⚠ Classes introuvables, plages horaires non créées");
            return;
        }

        UE algo   = ueRepository.findByCode("INF101").orElse(null);
        UE bdd    = ueRepository.findByCode("INF201").orElse(null);
        UE web    = ueRepository.findByCode("INF301").orElse(null);
        UE reseau = ueRepository.findByCode("INF401").orElse(null);
        UE ihm    = ueRepository.findByCode("INF701").orElse(null);
        UE comm   = ueRepository.findByCode("GEN101").orElse(null);
        UE sport  = ueRepository.findByCode("SPO101").orElse(null);
        UE maths  = ueRepository.findByCode("MAT101").orElse(null);

        Enseignant billong = getEnseignant("billong@carnetrouge.com");
        Enseignant pessa   = getEnseignant("pessa@carnetrouge.com");
        Enseignant mballa  = getEnseignant("mballa@carnetrouge.com");
        Enseignant nitcheu = getEnseignant("nitcheu@carnetrouge.com");
        Enseignant mboa    = getEnseignant("mboa@carnetrouge.com");
        Enseignant biamou  = getEnseignant("biamou@carnetrouge.com");
        Enseignant kamga   = getEnseignant("kamga@carnetrouge.com");

        // ── EDT L3 GI — semaine du 17 mars 2026 ──
        createPlage(LocalDate.of(2026,3,17), LocalTime.of(8,0),  LocalTime.of(10,0), "Amphi A",   "#dc2626", l3GI, algo,   billong);
        createPlage(LocalDate.of(2026,3,17), LocalTime.of(10,0), LocalTime.of(12,0), "Salle 12",  "#0f4c75", l3GI, bdd,    pessa);
        createPlage(LocalDate.of(2026,3,17), LocalTime.of(14,0), LocalTime.of(16,0), "Labo Info", "#1f6f3c", l3GI, web,    billong);

        createPlage(LocalDate.of(2026,3,18), LocalTime.of(8,0),  LocalTime.of(10,0), "Salle 8",   "#1f6f3c", l3GI, reseau, mballa);
        createPlage(LocalDate.of(2026,3,18), LocalTime.of(10,0), LocalTime.of(12,0), "Salle 5",   "#7d6608", l3GI, ihm,    nitcheu);
        createPlage(LocalDate.of(2026,3,18), LocalTime.of(14,0), LocalTime.of(15,0), "Amphi B",   "#7c2d12", l3GI, comm,   mboa);

        createPlage(LocalDate.of(2026,3,19), LocalTime.of(8,0),  LocalTime.of(10,0), "Terrain",   "#374151", l3GI, sport,  biamou);
        createPlage(LocalDate.of(2026,3,19), LocalTime.of(10,0), LocalTime.of(12,0), "Amphi A",   "#dc2626", l3GI, algo,   billong);

        createPlage(LocalDate.of(2026,3,20), LocalTime.of(8,0),  LocalTime.of(10,0), "Salle 12",  "#0f4c75", l3GI, bdd,    pessa);
        createPlage(LocalDate.of(2026,3,20), LocalTime.of(14,0), LocalTime.of(16,0), "Labo Info", "#1f6f3c", l3GI, web,    billong);

        // ── EDT L2 GI — même semaine ──
        createPlage(LocalDate.of(2026,3,17), LocalTime.of(8,0),  LocalTime.of(10,0), "Amphi C",  "#5b21b6", l2GI, maths, kamga);
        createPlage(LocalDate.of(2026,3,17), LocalTime.of(10,0), LocalTime.of(12,0), "Salle 3",  "#dc2626", l2GI, algo,  billong);
        createPlage(LocalDate.of(2026,3,18), LocalTime.of(8,0),  LocalTime.of(9,0),  "Amphi B",  "#7c2d12", l2GI, comm,  mboa);
        createPlage(LocalDate.of(2026,3,20), LocalTime.of(10,0), LocalTime.of(12,0), "Terrain",  "#374151", l2GI, sport, biamou);

        System.out.println("✅ Plages horaires créées");
    }

    // ─────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────

    private Specialite createOrUpdateSpecialite(String nom, String code, String description) {
        return specialiteRepository.findByCode(code)
                .orElseGet(() -> specialiteRepository.save(
                        Specialite.builder()
                                .nom(nom)
                                .code(code)
                                .description(description)
                                .createAt(LocalDateTime.now())
                                .updateAt(LocalDateTime.now())
                                .build()
                ));
    }

    private Classes createOrUpdateClasse(String nom, Specialite specialite) {
        return classesRepository.findByNom(nom)
                .orElseGet(() -> classesRepository.save(
                        Classes.builder()
                                .nom(nom)
                                .specialite(specialite)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));
    }

    private UE createOrUpdateUE(String nom, String code, String libelle, Long nbrCredit, Long dheure) {
        return ueRepository.findByCode(code)
                .orElseGet(() -> ueRepository.save(
                        UE.builder()
                                .nom(nom)
                                .code(code)
                                .libelle(libelle)
                                .nbrCredit(nbrCredit)
                                .dheure(dheure)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));
    }

    // ✅ Crée une plage seulement si aucun conflit n'existe
    private void createPlage(
            LocalDate jour, LocalTime debut, LocalTime fin,
            String salle, String couleur,
            Classes classe, UE ue, Enseignant enseignant) {

        // ✅ Guard — ignorer si un des paramètres est null
        if (classe == null || ue == null || enseignant == null) return;

        // ✅ Vérifier qu'il n'existe pas déjà une plage sur ce créneau pour cette classe
        boolean existe = plageHoraireRepository.existsConflitClasse(
                classe.getId(), jour, debut, fin);
        if (!existe) {
            plageHoraireRepository.save(
                    PlageHoraire.builder()
                            .jour(jour)
                            .heureDebut(debut)
                            .heureFin(fin)
                            .salle(salle)
                            .couleur(couleur)
                            .classe(classe)
                            .ue(ue)
                            .enseignant(enseignant)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
            );
        }
    }

    private Enseignant getEnseignant(String email) {
        return utilisateurRepository.findByEmail(email)
                .filter(u -> u instanceof Enseignant)
                .map(u -> (Enseignant) u)
                .orElse(null);
    }
}