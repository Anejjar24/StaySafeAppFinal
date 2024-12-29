package ma.ensaj.StaySafe.controller;


import ma.ensaj.StaySafe.dto.LocationDTO;
import ma.ensaj.StaySafe.entity.Location;
import ma.ensaj.StaySafe.entity.LocationStatus;
import ma.ensaj.StaySafe.entity.User;
import ma.ensaj.StaySafe.repository.UserRepository;
import ma.ensaj.StaySafe.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin
public class LocationController {
    @Autowired
    private LocationService locationService;
    @Autowired
    private UserRepository userRepository;

//    @PostMapping
//    public ResponseEntity<Location> create(@RequestBody Location location) {
//        return ResponseEntity.ok(locationService.save(location));
//    }
@PostMapping
public ResponseEntity<Location> create(@RequestBody LocationDTO locationDTO) {
    // Rechercher l'utilisateur par ID
    Optional<User> userOptional = userRepository.findById(locationDTO.getUserId());
    if (userOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    User user = userOptional.get();

    // Créer et associer la localisation
    Location location = new Location();
    location.setLatitude(locationDTO.getLatitude());
    location.setLongitude(locationDTO.getLongitude());
    location.setTimestamp(LocalDateTime.parse(locationDTO.getTimestamp()));
    location.setIsEmergency(locationDTO.getIsEmergency());
    location.setStatus(LocationStatus.valueOf(locationDTO.getStatus()));
    location.setUser(user); // Associer l'utilisateur

    Location savedLocation = locationService.save(location);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
}


    @GetMapping
    public ResponseEntity<List<Location>> getAll() {
        return ResponseEntity.ok(locationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> update(@PathVariable Long id, @RequestBody Location location) {
        return ResponseEntity.ok(locationService.update(id, location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.ok().build();
    }
}