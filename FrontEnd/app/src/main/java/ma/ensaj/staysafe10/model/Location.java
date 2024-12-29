package ma.ensaj.staysafe10.model;

public class Location {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String timestamp;
    private Boolean isEmergency;
    private Long userId;
    private String message;
    private String status;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public Boolean getIsEmergency() { return isEmergency; }
    public void setIsEmergency(Boolean emergency) { isEmergency = emergency; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}