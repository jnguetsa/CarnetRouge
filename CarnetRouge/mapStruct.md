# Cours sur MapStruct avec Spring Boot

## 1. C'est quoi MapStruct ?

MapStruct est un générateur de code Java qui automatise la conversion entre objets (Entity → DTO, DTO → Entity, etc.). Il génère le code de mapping à la **compilation**, ce qui le rend très performant (pas de réflexion à l'exécution contrairement à ModelMapper).

---

## 2. Installation

Dans ton `pom.xml` :

```xml
<dependencies>
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>1.5.5.Final</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <annotationProcessorPaths>
                    <!-- Lombok DOIT être avant MapStruct -->
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>1.18.30</version>
                    </path>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>1.5.5.Final</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

> ⚠️ **L'ordre Lombok → MapStruct est critique**, sinon MapStruct ne voit pas les getters/setters générés par Lombok.

---

## 3. Création des DTOs

### PermissionDTO
```java
package CarnetRouge.CarnetRouge.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean active;
}
```

### RoleDTO
```java
package CarnetRouge.CarnetRouge.DTO;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private Set<PermissionDTO> permissions;
}
```

### EnseignantDTO
```java
package CarnetRouge.CarnetRouge.DTO;

import lombok.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnseignantDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Date dateNaissance;
    private String grade;
    private String typeEnseignant;
    private Boolean active;
    private Set<RoleDTO> roles;
}
```

### EnseignantCreateDTO (pour la création, sans id ni roles)
```java
package CarnetRouge.CarnetRouge.DTO;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnseignantCreateDTO {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Date dateNaissance;
    private String grade;
    private String typeEnseignant;
}
```

---

## 4. Création des Mappers

### PermissionMapper
```java
package CarnetRouge.CarnetRouge.Mapper;

import CarnetRouge.CarnetRouge.DTO.PermissionDTO;
import CarnetRouge.CarnetRouge.Entity.Permission;
import org.mapstruct.*;

@Mapper(componentModel = "spring") // ← Spring gère le bean automatiquement
public interface PermissionMapper {

    // Entity → DTO
    PermissionDTO toDTO(Permission permission);

    // DTO → Entity
    @Mapping(target = "roles", ignore = true)       // on ignore la relation inverse
    @Mapping(target = "creatAt", ignore = true)     // géré automatiquement
    @Mapping(target = "updateAt", ignore = true)
    Permission toEntity(PermissionDTO dto);
}
```

### RoleMapper
```java
package CarnetRouge.CarnetRouge.Mapper;

import CarnetRouge.CarnetRouge.DTO.RoleDTO;
import CarnetRouge.CarnetRouge.Entity.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class}) // ← uses pour réutiliser PermissionMapper
public interface RoleMapper {

    RoleDTO toDTO(Role role);

    @Mapping(target = "creatAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    Role toEntity(RoleDTO dto);
}
```

### EnseignantMapper
```java
package CarnetRouge.CarnetRouge.Mapper;

import CarnetRouge.CarnetRouge.DTO.EnseignantCreateDTO;
import CarnetRouge.CarnetRouge.DTO.EnseignantDTO;
import CarnetRouge.CarnetRouge.Entity.Enseignant;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface EnseignantMapper {

    // Entity → DTO complet
    EnseignantDTO toDTO(Enseignant enseignant);

    // DTO création → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "active", constant = "true")       // valeur par défaut
    @Mapping(target = "firstLogin", constant = "true")
    @Mapping(target = "locked", constant = "false")
    @Mapping(target = "expired", constant = "false")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "phoneVerified", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Enseignant toEntity(EnseignantCreateDTO dto);

    // Mise à jour partielle : on met à jour une entité existante depuis un DTO
    // Les champs null du DTO ne sont PAS appliqués grâce à NullValuePropertyMappingStrategy
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(EnseignantDTO dto, @MappingTarget Enseignant enseignant);
}
```

---

## 5. Utilisation dans un Service

```java
package CarnetRouge.CarnetRouge.Service;

import CarnetRouge.CarnetRouge.DTO.EnseignantCreateDTO;
import CarnetRouge.CarnetRouge.DTO.EnseignantDTO;
import CarnetRouge.CarnetRouge.Entity.Enseignant;
import CarnetRouge.CarnetRouge.Mapper.EnseignantMapper;
import CarnetRouge.CarnetRouge.Repository.EnseignantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final EnseignantMapper enseignantMapper;

    // Récupérer tous les enseignants
    public List<EnseignantDTO> findAll() {
        return enseignantRepository.findAll()
                .stream()
                .map(enseignantMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Créer un enseignant
    public EnseignantDTO create(EnseignantCreateDTO dto) {
        Enseignant enseignant = enseignantMapper.toEntity(dto);
        // encoder le password avant save
        enseignant.setPassword(passwordEncoder.encode(dto.getPassword()));
        return enseignantMapper.toDTO(enseignantRepository.save(enseignant));
    }

    // Mise à jour partielle
    public EnseignantDTO update(Long id, EnseignantDTO dto) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

        enseignantMapper.updateEntityFromDTO(dto, enseignant); // ← @MappingTarget

        return enseignantMapper.toDTO(enseignantRepository.save(enseignant));
    }
}
```

---

## 6. Récapitulatif des annotations clés

| Annotation | Rôle |
|---|---|
| `@Mapper(componentModel = "spring")` | Crée un bean Spring injectable |
| `@Mapping(target = "x", ignore = true)` | Ignore un champ |
| `@Mapping(target = "x", source = "y")` | Renomme un champ |
| `@Mapping(target = "x", constant = "val")` | Valeur fixe |
| `@MappingTarget` | Mise à jour d'une entité existante |
| `@BeanMapping(nullValuePropertyMappingStrategy = IGNORE)` | Ne pas écraser avec null |
| `uses = {AutreMapper.class}` | Réutilise un autre mapper pour les objets imbriqués |

---

## 7. Ce que MapStruct génère automatiquement

Après compilation, tu trouveras dans `target/generated-sources` une classe comme :

```java
// Généré automatiquement - ne pas modifier
@Component
public class EnseignantMapperImpl implements EnseignantMapper {

    @Override
    public EnseignantDTO toDTO(Enseignant enseignant) {
        if (enseignant == null) return null;
        EnseignantDTO dto = new EnseignantDTO();
        dto.setId(enseignant.getId());
        dto.setNom(enseignant.getNom());
        // ... tous les champs
        return dto;
    }
}
```

C'est du code Java pur, sans réflexion → très rapide !

Quelques points importants à connaître en plus :

**Mapping entre champs de noms différents**
```java
@Mapping(target = "nomComplet", source = "nom")
EnseignantDTO toDTO(Enseignant enseignant);
```

**Mapping avec expression Java** quand la logique est simple :
```java
@Mapping(target = "nomComplet", expression = "java(enseignant.getNom() + ' ' + enseignant.getPrenom())")
EnseignantDTO toDTO(Enseignant enseignant);
```

**Méthode personnalisée** quand la logique est complexe. Tu peux ajouter une méthode `default` directement dans l'interface :
```java
@Mapper(componentModel = "spring")
public interface EnseignantMapper {

    EnseignantDTO toDTO(Enseignant enseignant);

    default String formatGrade(String grade) {
        if (grade == null) return "Non renseigné";
        return grade.toUpperCase();
    }
}
```

**Injection d'un service dans un mapper** avec `@Named` pour des cas comme l'encodage de mot de passe :
```java
@Mapper(componentModel = "spring", uses = {PasswordEncoderMapper.class})
public interface EnseignantMapper { ... }
```

**Mapper avec plusieurs sources** :
```java
@Mapping(source = "enseignant.nom", target = "nom")
@Mapping(source = "classe.libelle", target = "classe")
EnseignantDTO toDTO(Enseignant enseignant, Classe classe);
```

**Les pièges courants à éviter :**

- Oublier l'ordre **Lombok avant MapStruct** dans le pom.xml → MapStruct ne voit pas les getters/setters et génère un mapper vide
- Oublier `ignore = true` sur les relations bidirectionnelles (`@ManyToMany(mappedBy = ...)`) → risque de boucle infinie lors de la sérialisation
- Ne pas utiliser `uses` pour les objets imbriqués → MapStruct ne sait pas comment convertir `Set<Role>` en `Set<RoleDTO>` et lève une erreur à la compilation

**Débogage** : si quelque chose ne fonctionne pas, regarde toujours le fichier généré dans `target/generated-sources/annotations`. C'est là que tu verras exactement ce que MapStruct a produit et pourquoi un champ n'est pas mappé.