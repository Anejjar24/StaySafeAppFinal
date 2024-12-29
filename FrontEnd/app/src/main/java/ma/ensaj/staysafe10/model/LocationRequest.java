package ma.ensaj.staysafe10.model;
public class LocationRequest {
    private Double latitude;
    private Double longitude;

    public LocationRequest(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters
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
}