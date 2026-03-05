package CarnetRouge.CarnetRouge.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "type",
        length = 15,
        discriminatorType = DiscriminatorType.STRING
)
@EntityListeners(AuditingEntityListener.class)
public abstract class Utilisateurs implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String nom;
    protected String prenom;

    @Column(unique = true, nullable = false)
    protected String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(nullable = false)
    protected String password;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date dateNaissance;


    @Column( nullable = false)
    protected  String telephone;


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

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "utilisateurs_roles",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    protected Set<Role> roles = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles.isEmpty()) {
            return Collections.emptyList();
        }
        Set<GrantedAuthority> authorities= new HashSet<>();
        for (Role role : roles){

            if (Boolean.TRUE.equals(role.getActive())){
                authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
            }

            for(Permission perm : role.getPermissions()){
                if(Boolean.TRUE.equals(perm.getActive())){
                    authorities.add(new SimpleGrantedAuthority(perm.getName()));
                }
            }

        }
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return active;   // ou return active != null && active;
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
        return true;   // ou !someCredentialsExpiredField si tu en ajoutes un
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
