package CarnetRouge.CarnetRouge.GDU.Repository;

import CarnetRouge.CarnetRouge.GDU.Entity.Surveillant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SurveillantRepository extends JpaRepository<Surveillant, Long> {
    boolean existsByEmail(String email);
}
