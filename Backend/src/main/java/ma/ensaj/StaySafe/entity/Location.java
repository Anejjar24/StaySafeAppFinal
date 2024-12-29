package ma.ensaj.StaySafe.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
    private Boolean isEmergency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String message;
    @Enumerated(EnumType.STRING)
    private LocationStatus status = LocationStatus.ACTIVE;

}

