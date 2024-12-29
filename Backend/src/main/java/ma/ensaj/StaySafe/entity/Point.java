package ma.ensaj.StaySafe.entity;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "points")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "risk_zone_id")
    private RiskZone riskZone;
}