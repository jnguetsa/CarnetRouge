package CarnetRouge.CarnetRouge.Repository;

import CarnetRouge.CarnetRouge.Entity.Permission;
import CarnetRouge.CarnetRouge.Entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    boolean existsByName(String name);
    List<Permission> findByActiveTrue();
    Page<Permission> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page <Permission> findByActive(Boolean active, Pageable pageable);
}