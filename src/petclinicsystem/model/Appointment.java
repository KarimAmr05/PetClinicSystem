package petclinicsystem.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private int appointmentID;
    private LocalDateTime dateTime;
    private String status;
    private int vetId;
    private String vetName;

    public Appointment() {
        this(0, LocalDateTime.now(), "Scheduled", 0, "");
    }

    public Appointment(int appointmentID, LocalDateTime dateTime, String status, int vetId, String vetName) {
        this.appointmentID = appointmentID;
        this.dateTime = dateTime;
        this.status = status;
        this.vetId = vetId;
        this.vetName = vetName;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getStatus() {
        return status;
    }

    public int getVetId() {
        return vetId;
    }

    public String getVetName() {
        return vetName;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public String serialize() {
        return appointmentID + "~"
                + (dateTime == null ? "" : dateTime.format(FORMATTER)) + "~"
                + safe(status) + "~"
                + vetId + "~"
                + safe(vetName);
    }

    public static Appointment deserialize(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }

        String[] parts = data.split("~", -1);
        if (parts.length < 5) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            LocalDateTime dateTime = LocalDateTime.parse(parts[1].trim(), FORMATTER);
            String status = parts[2];
            int vetId = Integer.parseInt(parts[3].trim());
            String vetName = parts[4];
            return new Appointment(id, dateTime, status, vetId, vetName);
        } catch (Exception e) {
            return null;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    public String toString() {
        String date = dateTime == null ? "Unknown time" : dateTime.format(FORMATTER);
        return "Appointment ID: " + appointmentID
                + ", Date/Time: " + date
                + ", Status: " + status
                + ", Vet: " + vetName + " (ID: " + vetId + ")";
    }
}