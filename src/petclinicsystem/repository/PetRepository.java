package petclinicsystem.repository;

import petclinicsystem.model.Pet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PetRepository {

    public List<Pet> load(String filename) {
        List<Pet> pets = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return pets;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Pet pet = Pet.deserialize(line);
                if (pet != null) {
                    pets.add(pet);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading pets: " + e.getMessage());
        }

        return pets;
    }

    public void save(List<Pet> pets, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Pet pet : pets) {
                writer.write(pet.serialize());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving pets: " + e.getMessage());
        }
    }
}