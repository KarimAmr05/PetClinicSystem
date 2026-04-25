package petclinicsystem.model;

public class Prescription {
    private int id;
    private String medicineName;
    private String dosage;

    public Prescription() {
        this(0, "", "");
    }

    public Prescription(int id, String medicineName, String dosage) {
        this.id = id;
        this.medicineName = medicineName;
        this.dosage = dosage;
    }

    public int getId() {
        return id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String serialize() {
        return id + "~" + safe(medicineName) + "~" + safe(dosage);
    }

    public static Prescription deserialize(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }

        String[] parts = data.split("~", -1);
        if (parts.length < 3) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            return new Prescription(id, parts[1], parts[2]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    public String toString() {
        if (medicineName == null || medicineName.isBlank()) {
            return "No prescription";
        }
        return "Prescription ID: " + id + ", Medicine: " + medicineName + ", Dosage: " + dosage;
    }
}