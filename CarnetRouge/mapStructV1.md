# Cours complet sur MapStruct

---

## 1. C'est quoi MapStruct ?

MapStruct est un **générateur de code** qui crée automatiquement des classes de mapping entre objets Java (entités, DTOs, etc.) à la compilation.

```
Entité (BDD)  ←→  DTO (ce qu'on envoie/reçoit)
Utilisateurs  ←→  UtilisateurDTO
Enseignant    ←→  EnseignantDTO
```

### Pourquoi utiliser des DTOs ?

```
❌ Sans DTO → tu exposes directement ton entité
   → le mot de passe, les rôles internes, tout est visible

✅ Avec DTO → tu contrôles exactement ce que tu exposes
   → tu envoies seulement nom, prénom, email
```

---

## 2. Installation

```xml
<!-- pom.xml -->
<properties>
    <mapstruct.version>1.5.5.Final</mapstruct.version>
    <lombok.version>1.18.30</lombok.version>
</properties>

<dependencies>
    <!-- MapStruct -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${mapstruct.version}</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <annotationProcessorPaths>
                    <!-- ⚠️ Lombok DOIT être avant MapStruct -->
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${lombok.version}</version>
                    </path>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${mapstruct.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

---

## 3. Concepts de base

### Cas simple — champs identiques

```java
// Entité
public class Enseignant extends Utilisateurs {
    private String grade;
    private String typeEnseignant;
}

// DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnseignantDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String grade;
    private String typeEnseignant;
    private boolean active;
}

// Mapper
@Mapper(componentModel = "spring") // ← injecté comme un @Bean Spring
public interface EnseignantMapper {
    // ✅ Champs identiques → mapping automatique
    EnseignantDTO toDTO(Enseignant enseignant);
    Enseignant toEntity(EnseignantDTO dto);
}
```

MapStruct génère automatiquement :

```java
// Code généré par MapStruct à la compilation
@Component
public class EnseignantMapperImpl implements EnseignantMapper {

    @Override
    public EnseignantDTO toDTO(Enseignant enseignant) {
        if (enseignant == null) return null;

        EnseignantDTO dto = new EnseignantDTO();
        dto.setId(enseignant.getId());
        dto.setNom(enseignant.getNom());
        dto.setPrenom(enseignant.getPrenom());
        dto.setEmail(enseignant.getEmail());
        dto.setGrade(enseignant.getGrade());
        dto.setTypeEnseignant(enseignant.getTypeEnseignant());
        dto.setActive(enseignant.isActive());
        return dto;
    }
}
```

---

## 4. Les annotations essentielles

### `@Mapping` — champs avec noms différents

```java
// Entité
public class Utilisateurs {
    private String nom;      // ← nom dans l'entité
    private String email;
}

// DTO
public class UtilisateurDTO {
    private String lastName; // ← nom différent dans le DTO
    private String email;
}

// Mapper
@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    @Mapping(source = "nom", target = "lastName") // ← relie les deux
    UtilisateurDTO toDTO(Utilisateurs utilisateur);

    @Mapping(source = "lastName", target = "nom") // ← sens inverse
    Utilisateurs toEntity(UtilisateurDTO dto);
}
```

### `@Mapping(ignore = true)` — ignorer un champ

```java
@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    @Mapping(target = "password", ignore = true)  // ← ne jamais exposer
    @Mapping(target = "roles", ignore = true)      // ← ne pas exposer
    UtilisateurDTO toDTO(Utilisateurs utilisateur);
}
```

### `@Mapping(constant = "...")` — valeur fixe

```java
@Mapper(componentModel = "spring")
public interface EnseignantMapper {

    @Mapping(target = "type", constant = "ENSEIGNANT") // ← toujours cette valeur
    EnseignantDTO toDTO(Enseignant enseignant);
}
```

### `@Mapping(expression = "...")` — expression Java

```java
@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    @Mapping(
        target = "nomComplet",
        expression = "java(utilisateur.getNom() + ' ' + utilisateur.getPrenom())"
    )
    UtilisateurDTO toDTO(Utilisateurs utilisateur);
}
```

---

## 5. Mapping de listes

```java
@Mapper(componentModel = "spring")
public interface EnseignantMapper {

    EnseignantDTO toDTO(Enseignant enseignant);

    // ✅ MapStruct génère automatiquement le mapping de liste
    List<EnseignantDTO> toDTOList(List<Enseignant> enseignants);

    // ✅ Page → List (Spring Data)
    default List<EnseignantDTO> toDTOList(Page<Enseignant> page) {
        return toDTOList(page.getContent());
    }
}
```

---

## 6. Mapping avec objets imbriqués

```java
// Entités
public class Utilisateurs {
    private Set<Role> roles;
}

public class Role {
    private String name;
    private Set<Permission> permissions;
}

// DTOs
@Data
public class UtilisateurDTO {
    private String nom;
    private List<RoleDTO> roles;
}

@Data
public class RoleDTO {
    private String name;
    private List<String> permissions;
}

// Mapper
@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    UtilisateurDTO toDTO(Utilisateurs utilisateur);
    RoleDTO toRoleDTO(Role role);

    // ✅ Convertit Set<Permission> en List<String>
    default List<String> permissionsToStrings(Set<Permission> permissions) {
        if (permissions == null) return List.of();
        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toList());
    }
}
```

---

## 7. Mise à jour partielle — `@MappingTarget`

```java
// DTO de mise à jour
@Data
public class UpdateEnseignantDTO {
    private String nom;
    private String prenom;
    private String telephone;
    private String grade;
}

// Mapper
@Mapper(componentModel = "spring")
public interface EnseignantMapper {

    // ✅ Met à jour l'entité existante sans la recréer
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromDTO(UpdateEnseignantDTO dto, @MappingTarget Enseignant enseignant);
}

// Utilisation dans le service
@Service
@RequiredArgsConstructor
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final EnseignantMapper enseignantMapper;

    public EnseignantDTO modifier(Long id, UpdateEnseignantDTO dto) {
        Enseignant enseignant = enseignantRepository.findById(id).orElseThrow();

        // ✅ Met à jour seulement les champs du DTO
        enseignantMapper.updateFromDTO(dto, enseignant);

        return enseignantMapper.toDTO(enseignantRepository.save(enseignant));
    }
}
```

---

## 8. Mappers avec plusieurs sources

```java
// Deux entités à combiner
public class Enseignant { ... }
public class Role { ... }

// DTO combiné
@Data
public class EnseignantAvecRoleDTO {
    private String nom;
    private String email;
    private String roleName;
}

// Mapper avec plusieurs sources
@Mapper(componentModel = "spring")
public interface EnseignantMapper {

    @Mapping(source = "enseignant.nom",   target = "nom")
    @Mapping(source = "enseignant.email", target = "email")
    @Mapping(source = "role.name",        target = "roleName")
    EnseignantAvecRoleDTO toDTO(Enseignant enseignant, Role role);
}
```

---

## 9. Exemple complet pour CarnetRouge

### Les DTOs

```java
// DTO réponse (ce qu'on envoie)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EnseignantResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String grade;
    private String typeEnseignant;
    private boolean active;
    private LocalDateTime createdAt;
    private List<String> roles;
    private List<String> permissions;
}

// DTO création (ce qu'on reçoit)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateEnseignantDTO {
    @NotBlank private String nom;
    @NotBlank private String prenom;
    @Email    private String email;
    @NotBlank private String password;
    @NotBlank private String telephone;
    private String grade;
    private String typeEnseignant;
}

// DTO modification
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateEnseignantDTO {
    private String nom;
    private String prenom;
    private String telephone;
    private String grade;
    private String typeEnseignant;
}
```

### Le Mapper complet

```java
@Mapper(componentModel = "spring")
public interface EnseignantMapper {

    // ✅ Entité → DTO réponse
    @Mapping(target = "roles",       expression = "java(extractRoles(enseignant))")
    @Mapping(target = "permissions", expression = "java(extractPermissions(enseignant))")
    EnseignantResponseDTO toResponseDTO(Enseignant enseignant);

    // ✅ DTO création → Entité
    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "active",        constant = "true")
    @Mapping(target = "locked",        constant = "false")
    @Mapping(target = "expired",       constant = "false")
    @Mapping(target = "firstLogin",    constant = "true")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "phoneVerified", constant = "false")
    @Mapping(target = "roles",         ignore = true)
    @Mapping(target = "createdAt",     ignore = true)
    @Mapping(target = "updatedAt",     ignore = true)
    Enseignant toEntity(CreateEnseignantDTO dto);

    // ✅ DTO modification → Entité existante
    @Mapping(target = "id",         ignore = true)
    @Mapping(target = "email",      ignore = true)
    @Mapping(target = "password",   ignore = true)
    @Mapping(target = "roles",      ignore = true)
    @Mapping(target = "createdAt",  ignore = true)
    @Mapping(target = "updatedAt",  ignore = true)
    void updateFromDTO(UpdateEnseignantDTO dto, @MappingTarget Enseignant enseignant);

    // ✅ Liste
    List<EnseignantResponseDTO> toResponseDTOList(List<Enseignant> enseignants);

    // ✅ Méthodes helper
    default List<String> extractRoles(Utilisateurs u) {
        if (u.getRoles() == null) return List.of();
        return u.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    default List<String> extractPermissions(Utilisateurs u) {
        if (u.getRoles() == null) return List.of();
        return u.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .collect(Collectors.toList());
    }
}
```

### Utilisation dans le Service

```java
@Service
@RequiredArgsConstructor
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final EnseignantMapper enseignantMapper;
    private final PasswordEncoder passwordEncoder;

    // ✅ Lister avec pagination
    public Page<EnseignantResponseDTO> listerTous(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        return enseignantRepository.findAll(pageable)
                .map(enseignantMapper::toResponseDTO);
    }

    // ✅ Créer
    public EnseignantResponseDTO creer(CreateEnseignantDTO dto) {
        Enseignant enseignant = enseignantMapper.toEntity(dto);
        enseignant.setPassword(passwordEncoder.encode(dto.getPassword()));
        return enseignantMapper.toResponseDTO(enseignantRepository.save(enseignant));
    }

    // ✅ Modifier
    public EnseignantResponseDTO modifier(Long id, UpdateEnseignantDTO dto) {
        Enseignant enseignant = enseignantRepository.findById(id).orElseThrow();
        enseignantMapper.updateFromDTO(dto, enseignant);
        return enseignantMapper.toResponseDTO(enseignantRepository.save(enseignant));
    }

    // ✅ Trouver par id
    public EnseignantResponseDTO trouverParId(Long id) {
        return enseignantMapper.toResponseDTO(
            enseignantRepository.findById(id).orElseThrow()
        );
    }
}
```

---

## 10. Résumé des annotations

```
@Mapper(componentModel = "spring")  → bean Spring injectable
@Mapping(source, target)            → relie deux champs
@Mapping(ignore = true)             → ignore un champ
@Mapping(constant = "valeur")       → valeur fixe
@Mapping(expression = "java(...)")  → expression Java
@MappingTarget                      → mise à jour d'un objet existant
@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
                                    → ignore les champs null lors d'une update
```

---

## 11. Bonne pratique — ignore les nulls lors d'une update

```java
@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Mapping(target = "id",       ignore = true)
@Mapping(target = "email",    ignore = true)
@Mapping(target = "password", ignore = true)
void updateFromDTO(UpdateEnseignantDTO dto, @MappingTarget Enseignant enseignant);
```

Ainsi si `dto.getNom()` est `null`, le nom de l'enseignant **n'est pas écrasé**. Très utile pour les mises à jour partielles (PATCH).