package ma.ensaj.StaySafe.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class RiskZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String riskLevel; // LOW, MEDIUM, HIGH

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "risk_zone_id")
    private List<ma.ensaj.StaySafe.entity.Point> points;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
}