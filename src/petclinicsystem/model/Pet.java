package petclinicsystem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Pet {
    private int id;
    private int ownerId;
    private String name;
    private String breed;
    private int age;
    private HealthRecord healthRecord;
    private Prescription prescription;
    private List<Appointment> appointments;

    public Pet() {
        this(0, 0, "", "", 0, new HealthRecord(""), null);
    }

    public Pet(int id, int ownerId, String name, String breed, int age, HealthRecord healthRecord, Prescription prescription) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.healthRecord = healthRecord == null ? new HealthRecord("") : healthRecord;
        this.prescription = prescription;
        this.appointments = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public HealthRecord getHealthRecord() {
        return healthRecord;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHealthRecord(HealthRecord healthRecord) {
        this.healthRecord = healthRecord;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public String serialize() {
        String healthPart = healthRecord == null ? "" : healthRecord.serialize();
        String prescriptionPart = prescription == null ? "" : prescription.serialize();
        String appointmentPart = appointments == null || appointments.isEmpty()
                ? ""
                : appointments.stream()
                        .map(Appointment::serialize)
                        .collect(Collectors.joining(";"));

        return id + "|"
                + ownerId + "|"
                + safe(name) + "|"
                + safe(breed) + "|"
                + age + "|"
                + healthPart + "|"
                + prescriptionPart + "|"
                + appointmentPart;
    }

    public static Pet deserialize(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }

        String[] parts = data.split("\\|", -1);
        if (parts.length < 8) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            int ownerId = Integer.parseInt(parts[1].trim());
            String name = parts[2];
            String breed = parts[3];
            int age = Integer.parseInt(parts[4].trim());

            Pet pet = new Pet(id, ownerId, name, breed, age, new HealthRecord(""), null);

            if (!parts[5].isBlank()) {
                pet.setHealthRecord(HealthRecord.deserialize(parts[5]));
            }

            if (!parts[6].isBlank()) {
                pet.setPrescription(Prescription.deserialize(parts[6]));
            }

            if (!parts[7].isBlank()) {
                String[] appts = parts[7].split(";", -1);
                for (String a : appts) {
                    Appointment appointment = Appointment.deserialize(a);
                    if (appointment != null) {
                        pet.addAppointment(appointment);
                    }
                }
            }

            return pet;
        } catch (Exception e) {
            return null;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    public String toString() {
        return "Pet ID: " + id
                + ", Owner ID: " + ownerId
                + ", Name: " + name
                + ", Breed: " + breed
                + ", Age: " + age;
    }
}