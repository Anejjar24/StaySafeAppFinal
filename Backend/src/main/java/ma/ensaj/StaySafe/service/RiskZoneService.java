package ma.ensaj.StaySafe.service;

import ma.ensaj.StaySafe.entity.Point;
import ma.ensaj.StaySafe.entity.RiskZone;
import ma.ensaj.StaySafe.repository.RiskZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class RiskZoneService {
    @Autowired
    private RiskZoneRepository riskZoneRepository;

    public boolean isLocationInAnyRiskZone(Double latitude, Double longitude) {
        List<RiskZone> activeZones = riskZoneRepository.findByIsActiveTrue();

        for (RiskZone zone : activeZones) {
            if (isPointInPolygon(latitude, longitude, zone.getPoints())) {
                return true;
            }
        }
        return false;
    }


    public RiskZone createRiskZone(RiskZone riskZone) {
        riskZone.setCreatedAt(LocalDateTime.now());
        riskZone.setUpdatedAt(LocalDateTime.now());
        return riskZoneRepository.save(riskZone);
    }

    public List<RiskZone> getAllActiveRiskZones() {
        return riskZoneRepository.findByIsActiveTrue();
    }


    private boolean isPointInPolygon(Double latitude, Double longitude, List<ma.ensaj.StaySafe.entity.Point> polygon) {
        if (polygon == null || polygon.size() < 3) {
            return false;
        }

        boolean result = false;
        int j = polygon.size() - 1;

        for (int i = 0; i < polygon.size(); i++) {
            if (polygon.get(i).getLongitude() < longitude &&
                    polygon.get(j).getLongitude() >= longitude ||
                    polygon.get(j).getLongitude() < longitude &&
                            polygon.get(i).getLongitude() >= longitude) {

                if (polygon.get(i).getLatitude() +
                        (longitude - polygon.get(i).getLongitude()) /
                                (polygon.get(j).getLongitude() - polygon.get(i).getLongitude()) *
                                (polygon.get(j).getLatitude() - polygon.get(i).getLatitude()) < latitude) {
                    result = !result;
                }
            }
            j = i;
        }
        return result;
    }

}