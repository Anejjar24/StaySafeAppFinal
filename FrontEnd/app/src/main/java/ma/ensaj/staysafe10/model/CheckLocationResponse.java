package ma.ensaj.staysafe10.model;
public class CheckLocationResponse {
    private boolean isInRiskZone;
    private Double latitude;
    private Double longitude;
    private String checkedAt;

    // Getters and setters
    public boolean isInRiskZone() {
        return isInRiskZone;
    }

    public void setInRiskZone(boolean inRiskZone) {
        isInRiskZone = inRiskZone;
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

    public String getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(String checkedAt) {
        this.checkedAt = checkedAt;
    }
}