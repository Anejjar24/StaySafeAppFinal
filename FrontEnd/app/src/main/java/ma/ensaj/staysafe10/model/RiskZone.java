package ma.ensaj.staysafe10.model;


import java.util.ArrayList;
import java.util.List;

// RiskZone.java
public class RiskZone {
    private Long id;
    private String name;
    private String description;
    private String riskLevel;  // LOW, MEDIUM, HIGH
    private List<Point> points;
    private String createdAt;  // Using String instead of LocalDateTime for JSON serialization
    private String updatedAt;
    private boolean isActive;

    // Constructors
    public RiskZone() {
        this.points = new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Helper method to add a point to the risk zone
    public void addPoint(Point point) {
        if (this.points == null) {
            this.points = new ArrayList<>();
        }
        this.points.add(point);
    }

    // Helper method to check if the zone has any points
    public boolean hasPoints() {
        return points != null && !points.isEmpty();
    }
}