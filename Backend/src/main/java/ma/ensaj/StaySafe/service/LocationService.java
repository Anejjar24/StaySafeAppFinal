package ma.ensaj.StaySafe.service;


import jakarta.transaction.Transactional;
import ma.ensaj.StaySafe.entity.Location;
import ma.ensaj.StaySafe.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public Location save(Location location) {
        location.setTimestamp(LocalDateTime.now());
        return locationRepository.save(location);
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location findById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    public List<Location> findByUserId(Long userId) {
        return locationRepository.findByUserId(userId);
    }

    public void delete(Long id) {
        locationRepository.deleteById(id);
    }

    public Location update(Long id, Location location) {
        Location existingLocation = findById(id);
        existingLocation.setLatitude(location.getLatitude());
        existingLocation.setLongitude(location.getLongitude());
        existingLocation.setIsEmergency(location.getIsEmergency());
        existingLocation.setMessage(location.getMessage());
        existingLocation.setStatus(location.getStatus());
        return locationRepository.save(existingLocation);
    }
}