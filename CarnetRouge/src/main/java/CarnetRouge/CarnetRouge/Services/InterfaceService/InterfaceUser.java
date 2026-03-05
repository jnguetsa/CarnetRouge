package CarnetRouge.CarnetRouge.Services.InterfaceService;

import CarnetRouge.CarnetRouge.Entity.Utilisateurs;
import java.util.List;

public interface InterfaceUser {

    List<Utilisateurs> getUtilisateurs();

    Utilisateurs getUtilisateur(Long id);

    Utilisateurs findByNomAndPrenom(String nom, String prenom);

    Utilisateurs activerUtilisateur(Long id);

    Utilisateurs desactiverUtilisateur(Long id);

    void deleteUtilisateur(Long id);
}