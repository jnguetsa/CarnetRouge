# Cours JPA — Philosophie et construction des requêtes

---

## 1. C'est quoi JPA ?

JPA (Java Persistence API) est une **spécification** qui permet de mapper des objets Java vers des tables de base de données. Spring Data JPA est l'implémentation qui simplifie tout ça.

```
Classe Java  ←→  Table BDD
Attribut     ←→  Colonne
Instance     ←→  Ligne
```

---

## 2. Les trois façons d'écrire des requêtes

### Méthode 1 — Requêtes dérivées (Derived Queries)

C'est la philosophie principale de Spring Data JPA : **tu écris le nom de la méthode, Spring génère le SQL automatiquement**.

```
findBy + [Propriété] + [Condition] + [Opérateur] + [Propriété]
```

```java
// Spring génère → SELECT * FROM utilisateurs WHERE email = ?
Optional<Utilisateurs> findByEmail(String email);

// Spring génère → SELECT * FROM utilisateurs WHERE nom = ? AND active = true
List<Utilisateurs> findByNomAndActiveTrue(String nom);

// Spring génère → SELECT * FROM utilisateurs WHERE email = ? OR nom = ?
List<Utilisateurs> findByEmailOrNom(String email, String nom);
```

---

### Méthode 2 — JPQL avec `@Query`

Tu écris toi-même la requête mais en langage **orienté objet** (tu parles des classes, pas des tables) :

```java
// JPQL → parle des classes Java
@Query("SELECT u FROM Utilisateurs u WHERE u.email = :email")
Optional<Utilisateurs> trouverParEmail(@Param("email") String email);

// ⚠️ Différence avec SQL natif
// SQL natif → SELECT * FROM utilisateurs WHERE email = ?
// JPQL      → SELECT u FROM Utilisateurs u WHERE u.email = ?
//                           ↑ nom de la classe, pas de la table
```

### Méthode 3 — SQL natif avec `@Query(nativeQuery = true)`

```java
@Query(value = "SELECT * FROM utilisateurs WHERE email = ?1", nativeQuery = true)
Optional<Utilisateurs> trouverParEmailNatif(String email);
```

---

## 3. Les mots-clés des requêtes dérivées

### Préfixes

```java
findBy...       // SELECT
existsBy...     // SELECT (retourne boolean)
countBy...      // SELECT COUNT
deleteBy...     // DELETE
```

### Conditions

```java
// Égalité
findByNom(String nom)                    // WHERE nom = ?

// Comparaison
findByScoreGreaterThan(int score)        // WHERE score > ?
findByScoreLessThan(int score)           // WHERE score < ?
findByScoreGreaterThanEqual(int score)   // WHERE score >= ?
findByScoreBetween(int min, int max)     // WHERE score BETWEEN ? AND ?

// Texte
findByNomContaining(String mot)          // WHERE nom LIKE %mot%
findByNomStartingWith(String prefix)     // WHERE nom LIKE prefix%
findByNomEndingWith(String suffix)       // WHERE nom LIKE %suffix
findByNomIgnoreCase(String nom)          // WHERE LOWER(nom) = LOWER(?)
findByNomContainingIgnoreCase(String m)  // WHERE LOWER(nom) LIKE LOWER(%m%)

// Null
findByTelephoneIsNull()                  // WHERE telephone IS NULL
findByTelephoneIsNotNull()               // WHERE telephone IS NOT NULL

// Boolean
findByActiveTrue()                       // WHERE active = true
findByActiveFalse()                      // WHERE active = false

// Collection
findByNomIn(List<String> noms)           // WHERE nom IN (?, ?, ?)
findByNomNotIn(List<String> noms)        // WHERE nom NOT IN (?, ?, ?)
```

### Opérateurs logiques

```java
// ET
findByNomAndPrenom(String nom, String prenom)
// WHERE nom = ? AND prenom = ?

// OU
findByNomOrEmail(String nom, String email)
// WHERE nom = ? OR email = ?

// Combinaison
findByNomAndActiveTrueOrEmail(String nom, String email)
// WHERE (nom = ? AND active = true) OR email = ?
```

### Tri et limite

```java
findByActiveTrueOrderByNomAsc()          // ORDER BY nom ASC
findByActiveTrueOrderByNomDesc()         // ORDER BY nom DESC
findByActiveTrueOrderByNomAscPrenomDesc()// ORDER BY nom ASC, prenom DESC

findFirstByOrderByCreatedAtDesc()        // LIMIT 1 ORDER BY created_at DESC
findTop3ByOrderByScoreDesc()             // LIMIT 3 ORDER BY score DESC
```

---

## 4. Pagination et tri

### Pageable

```java
// Repository
Page<Utilisateurs> findByActiveTrue(Pageable pageable);

// Utilisation dans le service
Pageable pageable = PageRequest.of(
    0,              // numéro de page (commence à 0)
    10,             // taille de la page
    Sort.by("nom").ascending()
);

Page<Utilisateurs> page = utilisateurRepository.findByActiveTrue(pageable);

// Infos disponibles sur la page
page.getContent();          // List<Utilisateurs> → les données
page.getTotalElements();    // nombre total d'éléments
page.getTotalPages();       // nombre total de pages
page.getNumber();           // numéro de la page actuelle
page.isFirst();             // première page ?
page.isLast();              // dernière page ?
page.hasNext();             // page suivante ?
page.hasPrevious();         // page précédente ?
```

---

## 5. Relations et FetchType

### LAZY vs EAGER

```java
// LAZY → charge les données seulement quand on y accède
// ⚠️ Peut causer LazyInitializationException hors session
@ManyToMany(fetch = FetchType.LAZY)
private Set<Role> roles;

// EAGER → charge les données immédiatement avec l'entité parente
// ✅ Toujours disponible, mais peut charger trop de données
@ManyToMany(fetch = FetchType.EAGER)
private Set<Role> roles;
```

### Règle générale

```
@ManyToOne  → EAGER par défaut
@OneToOne   → EAGER par défaut
@OneToMany  → LAZY par défaut
@ManyToMany → LAZY par défaut
```

---

## 6. CascadeType

Définit ce qui se passe sur les entités liées quand tu agis sur l'entité principale :

```java
@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
private Set<Role> roles;
```

```
CascadeType.PERSIST  → si tu sauvegardes A, sauvegarde aussi B
CascadeType.MERGE    → si tu modifies A, modifie aussi B
CascadeType.REMOVE   → si tu supprimes A, supprime aussi B ⚠️ dangereux
CascadeType.REFRESH  → si tu rafraichis A, rafraichis aussi B
CascadeType.ALL      → tout ce qui précède
```

---

## 7. Exemples concrets pour CarnetRouge

```java
// 🔍 Trouver un étudiant par matricule
Optional<Etudiant> findByMatricule(String matricule);

// 🔍 Chercher des étudiants par niveau
List<Etudiant> findByNiveau(TypeNiveau niveau);

// 🔍 Chercher des utilisateurs actifs par nom ou prénom
Page<Utilisateurs> findByActiveTrueAndNomContainingIgnoreCaseOrActiveTrueAndPrenomContainingIgnoreCase(
    String nom, String prenom, Pageable pageable);

// 🔍 Compter les étudiants par niveau
long countByNiveau(TypeNiveau niveau);

// 🔍 Vérifier si un email existe
boolean existsByEmail(String email);

// 🔍 Supprimer par email
void deleteByEmail(String email);

// 🔍 Les 5 derniers inscrits
List<Utilisateurs> findTop5ByOrderByCreatedAtDesc();

// 🔍 Enseignants permanents actifs triés par nom
List<Enseignant> findByTypeEnseignantAndActiveTrueOrderByNomAsc(String type);
```

---

## 8. Résumé de la philosophie

```
Nom de méthode = Préfixe + By + Propriété + Condition + Opérateur + ...

findBy       → SELECT
existsBy     → EXISTS
countBy      → COUNT
deleteBy     → DELETE

And / Or     → opérateurs logiques
IgnoreCase   → insensible à la casse
Containing   → LIKE %valeur%
StartingWith → LIKE valeur%
EndingWith   → LIKE %valeur
IsNull       → IS NULL
IsNotNull    → IS NOT NULL
True / False → = true / = false
Between      → BETWEEN
GreaterThan  → >
LessThan     → 
In           → IN (...)
OrderBy      → ORDER BY
Top / First  → LIMIT
```

La règle d'or : **si le nom de la méthode décrit clairement ce qu'elle fait, Spring sait générer le SQL.**

Voici des exemples de requêtes JPA basées sur tes entités réelles :

---

## UtilisateurRepository

```java
public interface UtilisateurRepository extends JpaRepository<Utilisateurs, Long> {

    // 🔍 Par email
    Optional<Utilisateurs> findByEmail(String email);
    boolean existsByEmail(String email);

    // 🔍 Actifs / inactifs
    List<Utilisateurs> findByActiveTrue();
    List<Utilisateurs> findByActiveFalse();
    Page<Utilisateurs> findByActiveTrue(Pageable pageable);

    // 🔍 Recherche par nom ou prénom
    Page<Utilisateurs> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
        String nom, String prenom, Pageable pageable);

    // 🔍 Comptes verrouillés
    List<Utilisateurs> findByLockedTrue();

    // 🔍 Première connexion non faite
    List<Utilisateurs> findByFirstLoginTrue();

    // 🔍 Email non vérifié
    List<Utilisateurs> findByEmailVerifiedFalse();

    // 🔍 Les 5 derniers inscrits
    List<Utilisateurs> findTop5ByOrderByCreatedAtDesc();

    // 🔍 Inscrits entre deux dates
    List<Utilisateurs> findByCreatedAtBetween(LocalDateTime debut, LocalDateTime fin);

    // 🔍 Compter les actifs
    long countByActiveTrue();

    // 🔍 Compter les verrouillés
    long countByLockedTrue();
}
```

---

## EnseignantRepository

```java
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    // 🔍 Par email
    Optional<Enseignant> findByEmail(String email);
    boolean existsByEmail(String email);

    // 🔍 Par grade
    List<Enseignant> findByGrade(String grade);
    List<Enseignant> findByGradeIgnoreCase(String grade);

    // 🔍 Par type (Permanent, Vacataire, etc.)
    List<Enseignant> findByTypeEnseignant(String type);

    // 🔍 Permanents actifs triés par nom
    List<Enseignant> findByTypeEnseignantAndActiveTrueOrderByNomAsc(String type);

    // 🔍 Recherche par nom ou prénom
    Page<Enseignant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
        String nom, String prenom, Pageable pageable);

    // 🔍 Par grade et actifs
    List<Enseignant> findByGradeAndActiveTrue(String grade);

    // 🔍 Compter par type
    long countByTypeEnseignant(String type);

    // 🔍 Compter par grade
    long countByGrade(String grade);

    // 🔍 Enseignants dont le grade contient un mot
    List<Enseignant> findByGradeContainingIgnoreCase(String mot);

    // 🔍 Les 10 derniers enseignants inscrits
    List<Enseignant> findTop10ByOrderByCreatedAtDesc();
}
```

---

## EtudiantRepository

```java
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    // 🔍 Par matricule
    Optional<Etudiant> findByMatricule(String matricule);
    boolean existsByMatricule(String matricule);

    // 🔍 Par niveau
    List<Etudiant> findByNiveau(TypeNiveau niveau);
    Page<Etudiant> findByNiveau(TypeNiveau niveau, Pageable pageable);

    // 🔍 Par niveau et actifs triés par nom
    List<Etudiant> findByNiveauAndActiveTrueOrderByNomAsc(TypeNiveau niveau);

    // 🔍 Recherche par nom ou prénom
    Page<Etudiant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
        String nom, String prenom, Pageable pageable);

    // 🔍 Recherche combinée nom/prenom + niveau
    Page<Etudiant> findByNiveauAndNomContainingIgnoreCase(
        TypeNiveau niveau, String nom, Pageable pageable);

    // 🔍 Compter par niveau
    long countByNiveau(TypeNiveau niveau);

    // 🔍 Première connexion non faite
    List<Etudiant> findByFirstLoginTrueAndActiveTrue();

    // 🔍 Les 10 derniers inscrits
    List<Etudiant> findTop10ByOrderByCreatedAtDesc();
}
```

---

## RoleRepository

```java
public interface RoleRepository extends JpaRepository<Role, Long> {

    // 🔍 Par nom exact
    Optional<Role> findByName(String name);
    boolean existsByName(String name);

    // 🔍 Actifs
    List<Role> findByActiveTrue();
    Page<Role> findByActiveTrue(Pageable pageable);

    // 🔍 Recherche par nom
    Page<Role> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // 🔍 Compter les actifs
    long countByActiveTrue();
}
```

---

## Comment les utiliser dans un Service

```java
@Service
@RequiredArgsConstructor
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;

    // ✅ Liste paginée
    public Page<Enseignant> listerTous(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        return enseignantRepository.findAll(pageable);
    }

    // ✅ Recherche par nom
    public Page<Enseignant> rechercher(String mot, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return enseignantRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
            mot, mot, pageable);
    }

    // ✅ Par type
    public List<Enseignant> parType(String type) {
        return enseignantRepository.findByTypeEnseignantAndActiveTrueOrderByNomAsc(type);
    }

    // ✅ Statistiques
    public Map<String, Long> statistiques() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total",      enseignantRepository.count());
        stats.put("actifs",     enseignantRepository.countByActiveTrue()); // ← héritée
        stats.put("permanents", enseignantRepository.countByTypeEnseignant("Permanent"));
        stats.put("vacataires", enseignantRepository.countByTypeEnseignant("Vacataire"));
        return stats;
    }
}
```

---

## Règle d'or rappel

```
findBy   → SELECT        Page<T>  → résultat paginé
existsBy → EXISTS        List<T>  → liste complète
countBy  → COUNT         long     → nombre
deleteBy → DELETE        Optional → 0 ou 1 résultat

And / Or / True / False / Null / Containing / IgnoreCase / Between / OrderBy / Top
```

Tu veux qu'on approfondisse les `@Query` JPQL ou les relations entre entités ?