package petclinicsystem.model;

public class HealthRecord {
    private String healthStatus;

    public HealthRecord() {
        this("");
    }

    public HealthRecord(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String serialize() {
        return healthStatus == null ? "" : healthStatus;
    }

    public static HealthRecord deserialize(String data) {
        return new HealthRecord(data == null ? "" : data);
    }

    @Override
    public String toString() {
        return healthStatus == null || healthStatus.isBlank()
                ? "No health record"
                : "Health Status: " + healthStatus;
    }
}