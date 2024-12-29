package ma.ensaj.StaySafe.dto;

import lombok.Data;
@Data
public class LocationDTO {
    private Double latitude;
    private Double longitude;
    private String timestamp;
    private Boolean isEmergency;
    private String status;
    private Long userId; // ID de l'utilisateur
}
