package ma.ensaj.StaySafe.controller;
import lombok.RequiredArgsConstructor;
import ma.ensaj.StaySafe.entity.Contact;
import ma.ensaj.StaySafe.entity.RiskZone;
import ma.ensaj.StaySafe.repository.RiskZoneRepository;
import ma.ensaj.StaySafe.service.ContactService;
import ma.ensaj.StaySafe.service.RiskZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/risk-zones")
@CrossOrigin
public class RiskZoneController {
    @Autowired
    private RiskZoneService riskZoneService;
    @Autowired
    private RiskZoneRepository riskZoneRepository;

    @PostMapping
    public ResponseEntity<RiskZone> createRiskZone(@RequestBody RiskZone riskZone) {
        try {
            RiskZone createdZone = riskZoneService.createRiskZone(riskZone);
            return new ResponseEntity<>(createdZone, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<RiskZone>> getAllActiveRiskZones() {
        try {
            List<RiskZone> zones = riskZoneService.getAllActiveRiskZones();
            return new ResponseEntity<>(zones, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<RiskZone> updateRiskZone(
            @PathVariable Long id,
            @RequestBody RiskZone riskZone) {
        try {
            Optional<RiskZone> existingZone = riskZoneRepository.findById(id);
            if (existingZone.isPresent()) {
                riskZone.setId(id);
                riskZone.setUpdatedAt(LocalDateTime.now());
                RiskZone updatedZone = riskZoneService.createRiskZone(riskZone);
                return new ResponseEntity<>(updatedZone, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRiskZone(@PathVariable Long id) {
        try {
            Optional<RiskZone> zone = riskZoneRepository.findById(id);
            if (zone.isPresent()) {
                // Soft delete - just mark as inactive
                RiskZone existingZone = zone.get();
                existingZone.setActive(false);
                existingZone.setUpdatedAt(LocalDateTime.now());
                riskZoneService.createRiskZone(existingZone);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiskZone> getRiskZoneById(@PathVariable Long id) {
        try {
            Optional<RiskZone> zone = riskZoneRepository.findById(id);
            if (zone.isPresent()) {
                return new ResponseEntity<>(zone.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkLocation(@RequestBody Map<String, Double> location) {
        try {
            Double latitude = location.get("latitude");
            Double longitude = location.get("longitude");

            boolean isInRiskZone = riskZoneService.isLocationInAnyRiskZone(latitude, longitude);
            Map<String, Object> response = new HashMap<>();
            response.put("isInRiskZone", isInRiskZone);
            response.put("latitude", latitude);
            response.put("longitude", longitude);
            response.put("checkedAt", LocalDateTime.now());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @GetMapping("/check")
//    public ResponseEntity<Map<String, Object>> checkLocation(
//            @RequestParam Double latitude,
//            @RequestParam Double longitude) {
//        try {
//            boolean isInRiskZone = riskZoneService.isLocationInAnyRiskZone(latitude, longitude);
//            Map<String, Object> response = new HashMap<>();
//            response.put("isInRiskZone", isInRiskZone);
//            response.put("latitude", latitude);
//            response.put("longitude", longitude);
//            response.put("checkedAt", LocalDateTime.now());
//
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}