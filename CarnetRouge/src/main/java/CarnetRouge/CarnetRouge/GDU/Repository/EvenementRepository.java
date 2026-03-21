package CarnetRouge.CarnetRouge.GDU.Repository;

import CarnetRouge.CarnetRouge.GDU.Entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement , Long > {
    // ✅ Trouver tous les événements d'une séance
    List<Evenement> findByPlageHoraireId(Long plageHoraireId);
}
