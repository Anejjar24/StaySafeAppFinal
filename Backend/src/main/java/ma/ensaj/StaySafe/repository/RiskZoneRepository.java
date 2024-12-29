package ma.ensaj.StaySafe.repository;

import ma.ensaj.StaySafe.entity.RiskZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskZoneRepository extends JpaRepository<RiskZone, Long> {
    List<RiskZone> findByIsActiveTrue();
}