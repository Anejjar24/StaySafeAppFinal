package ma.ensaj.StaySafe.repository;


import ma.ensaj.StaySafe.entity.Location;
import ma.ensaj.StaySafe.entity.LocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByUserId(Long userId);
    List<Location> findByStatus(LocationStatus status);
}
