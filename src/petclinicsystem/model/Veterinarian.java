package petclinicsystem.model;

public class Veterinarian extends User {
    private int id;
    private String name;

    public Veterinarian() {
        this(0, "");
    }

    public Veterinarian(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String serialize() {
        return id + "|" + safe(name);
    }

    public static Veterinarian deserialize(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }

        String[] parts = data.split("\\|", -1);
        if (parts.length < 2) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            return new Veterinarian(id, parts[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    public void showMenu() {
        System.out.println("\n===== Veterinarian Menu =====");
        System.out.println("1. Update Health Record");
        System.out.println("2. Provide Prescription");
        System.out.println("3. View Pet Medical History");
        System.out.println("4. Back");
    }

    @Override
    public String toString() {
        return "Veterinarian ID: " + id + ", Name: " + name;
    }
}