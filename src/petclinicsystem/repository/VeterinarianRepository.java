package petclinicsystem.repository;

import petclinicsystem.model.Veterinarian;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VeterinarianRepository {

    public List<Veterinarian> load(String filename) {
        List<Veterinarian> vets = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return vets;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Veterinarian vet = Veterinarian.deserialize(line);
                if (vet != null) {
                    vets.add(vet);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading veterinarians: " + e.getMessage());
        }

        return vets;
    }

    public void save(List<Veterinarian> vets, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Veterinarian vet : vets) {
                writer.write(vet.serialize());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving veterinarians: " + e.getMessage());
        }
    }
}