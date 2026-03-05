package CarnetRouge.CarnetRouge.Services.InterfaceService;

import CarnetRouge.CarnetRouge.Entity.Permission;
import CarnetRouge.CarnetRouge.Entity.Role;

import java.util.List;

public interface ServiceInterface {

    List<Permission> getPermissions();
    List<Role> getRole();

    Permission getPermission(Permission permission ,Long id);
    Permission activatePermission(Long id);
    Permission deactivatePermission(Long id);
    Role activateRole(Long id);
    Role deactivateRole(Long id);
    void deleteUtilisateur(Long id);

}
