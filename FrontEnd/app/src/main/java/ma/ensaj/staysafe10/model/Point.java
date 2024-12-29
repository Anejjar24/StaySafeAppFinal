package ma.ensaj.staysafe10.model;
public class Point {
    private Long id;
    private Double latitude;
    private Double longitude;
    private Long riskZoneId;  // Pour la relation avec RiskZone

    // Constructors
    public Point() {}

    public Point(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getRiskZoneId() {
        return riskZoneId;
    }

    public void setRiskZoneId(Long riskZoneId) {
        this.riskZoneId = riskZoneId;
    }
}
