package petclinicsystem.model;

import java.util.ArrayList;
import java.util.List;

public class PetOwner extends User {
    private int id;
    private String name;
    private String email;
    private List<Pet> pets;

    public PetOwner() {
        this(0, "", "");
    }

    public PetOwner(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pets = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public Pet getPetByName(String petName) {
        for (Pet pet : pets) {
            if (pet.getName() != null && pet.getName().equalsIgnoreCase(petName)) {
                return pet;
            }
        }
        return null;
    }

    public String serialize() {
        return id + "|" + safe(name) + "|" + safe(email);
    }

    public static PetOwner deserialize(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }

        String[] parts = data.split("\\|", -1);
        if (parts.length < 3) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            return new PetOwner(id, parts[1], parts[2]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    public void showMenu() {
        System.out.println("\n===== Pet Owner Menu =====");
        System.out.println("1. Add Pet");
        System.out.println("2. Book Appointment");
        System.out.println("3. View Pet Medical History");
        System.out.println("4. List My Pets");
        System.out.println("5. Back");
    }

    @Override
    public String toString() {
        return "Pet Owner ID: " + id
                + ", Name: " + name
                + ", Email: " + email
                + ", Pets: " + pets.size();
    }
}