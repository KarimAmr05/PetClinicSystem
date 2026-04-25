package petclinicsystem.repository;

import petclinicsystem.model.PetOwner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PetOwnerRepository {

    public List<PetOwner> load(String filename) {
        List<PetOwner> owners = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return owners;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PetOwner owner = PetOwner.deserialize(line);
                if (owner != null) {
                    owners.add(owner);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading pet owners: " + e.getMessage());
        }

        return owners;
    }

    public void save(List<PetOwner> owners, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (PetOwner owner : owners) {
                writer.write(owner.serialize());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving pet owners: " + e.getMessage());
        }
    }
}