package CarnetRouge.CarnetRouge.GDU.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 15, discriminatorType = DiscriminatorType.STRING)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "utilisateurs")  // Vérifie que le nom de la table est correct
public abstract class Utilisateurs implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected String nom;

    @Column(nullable = false)
    protected String prenom;

    @Column(unique = true, nullable = false)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dateNaissance;

    @Column(nullable = false)
    protected String telephone;

    @Column(nullable = false)
    protected boolean active = true;

    protected boolean firstLogin = true;

    @Column(nullable = false)
    protected boolean locked = false;

    @Column(nullable = false)
    protected boolean expired = false;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    protected LocalDateTime updatedAt;

    protected Boolean emailVerified = false;
    protected Boolean phoneVerified = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "utilisateurs_role",  // Nom exact de la table de jointure
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    protected Set<Role> roles = new HashSet<>();

    // ────────────────────────────────────────────────
    // Implémentation de UserDetails
    // ────────────────────────────────────────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles.isEmpty()) {
            return Collections.emptyList();
        }

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : roles) {
            if (Boolean.TRUE.equals(role.getActive())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            }

            for (Permission perm : role.getPermissions()) {
                if (Boolean.TRUE.equals(perm.getActive())) {
                    authorities.add(new SimpleGrantedAuthority(perm.getName()));
                }
            }
        }

        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
