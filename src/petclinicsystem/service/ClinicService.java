package petclinicsystem.service;

import petclinicsystem.model.Appointment;
import petclinicsystem.model.HealthRecord;
import petclinicsystem.model.Pet;
import petclinicsystem.model.PetOwner;
import petclinicsystem.model.Prescription;
import petclinicsystem.model.Veterinarian;
import petclinicsystem.repository.PetOwnerRepository;
import petclinicsystem.repository.PetRepository;
import petclinicsystem.repository.VeterinarianRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClinicService {
    private static final String OWNERS_FILE = "petOwners.txt";
    private static final String VETS_FILE = "veterinarians.txt";
    private static final String PETS_FILE = "pets.txt";

    private final PetOwnerRepository petOwnerRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final PetRepository petRepository;

    private final List<PetOwner> petOwners;
    private final List<Veterinarian> veterinarians;
    private final List<Pet> pets;

    public ClinicService() {
        this.petOwnerRepository = new PetOwnerRepository();
        this.veterinarianRepository = new VeterinarianRepository();
        this.petRepository = new PetRepository();
        this.petOwners = new ArrayList<>();
        this.veterinarians = new ArrayList<>();
        this.pets = new ArrayList<>();
    }

    public void loadAll() {
        petOwners.clear();
        veterinarians.clear();
        pets.clear();

        petOwners.addAll(petOwnerRepository.load(OWNERS_FILE));
        veterinarians.addAll(veterinarianRepository.load(VETS_FILE));
        pets.addAll(petRepository.load(PETS_FILE));

        for (Pet pet : pets) {
            PetOwner owner = findPetOwnerById(pet.getOwnerId());
            if (owner != null) {
                owner.addPet(pet);
            }
        }
    }

    public void saveAll() {
        petOwnerRepository.save(petOwners, OWNERS_FILE);
        veterinarianRepository.save(veterinarians, VETS_FILE);
        petRepository.save(pets, PETS_FILE);
    }

    public List<PetOwner> getPetOwners() {
        return petOwners;
    }

    public List<Veterinarian> getVeterinarians() {
        return veterinarians;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public PetOwner addPetOwner(String name, String email) {
        PetOwner owner = new PetOwner(nextOwnerId(), name, email);
        petOwners.add(owner);
        return owner;
    }

    public boolean removePetOwner(int ownerId) {
        PetOwner owner = findPetOwnerById(ownerId);
        if (owner == null) {
            return false;
        }

        petOwners.remove(owner);
        pets.removeIf(pet -> pet.getOwnerId() == ownerId);
        return true;
    }

    public PetOwner findPetOwnerById(int ownerId) {
        for (PetOwner owner : petOwners) {
            if (owner.getId() == ownerId) {
                return owner;
            }
        }
        return null;
    }

    public PetOwner findPetOwnerByName(String name) {
        for (PetOwner owner : petOwners) {
            if (owner.getName() != null && owner.getName().equalsIgnoreCase(name)) {
                return owner;
            }
        }
        return null;
    }

    public Veterinarian addVeterinarian(String name) {
        Veterinarian vet = new Veterinarian(nextVeterinarianId(), name);
        veterinarians.add(vet);
        return vet;
    }

    public boolean removeVeterinarian(int vetId) {
        Veterinarian vet = findVeterinarianById(vetId);
        if (vet == null) {
            return false;
        }

        veterinarians.remove(vet);
        return true;
    }

    public Veterinarian findVeterinarianById(int vetId) {
        for (Veterinarian vet : veterinarians) {
            if (vet.getId() == vetId) {
                return vet;
            }
        }
        return null;
    }

    public Veterinarian findVeterinarianByName(String name) {
        for (Veterinarian vet : veterinarians) {
            if (vet.getName() != null && vet.getName().equalsIgnoreCase(name)) {
                return vet;
            }
        }
        return null;
    }

    public Pet addPetToOwner(int ownerId, String name, String breed, int age) {
        PetOwner owner = findPetOwnerById(ownerId);
        if (owner == null) {
            return null;
        }

        Pet pet = new Pet(nextPetId(), ownerId, name, breed, age, new HealthRecord(""), null);
        owner.addPet(pet);
        pets.add(pet);
        return pet;
    }

    public List<Pet> getPetsForOwner(int ownerId) {
        PetOwner owner = findPetOwnerById(ownerId);
        if (owner == null) {
            return Collections.emptyList();
        }
        return owner.getPets();
    }

    public Pet findPetByOwnerAndName(int ownerId, String petName) {
        PetOwner owner = findPetOwnerById(ownerId);
        if (owner == null) {
            return null;
        }
        return owner.getPetByName(petName);
    }

    public Appointment bookAppointment(int ownerId, String petName, int vetId, LocalDateTime dateTime) {
        Pet pet = findPetByOwnerAndName(ownerId, petName);
        Veterinarian vet = findVeterinarianById(vetId);

        if (pet == null || vet == null) {
            return null;
        }

        Appointment appointment = new Appointment(
                nextAppointmentId(pet),
                dateTime,
                "Scheduled",
                vet.getId(),
                vet.getName()
        );

        pet.addAppointment(appointment);
        return appointment;
    }

    public boolean updateHealthRecord(int ownerId, String petName, String healthStatus) {
        Pet pet = findPetByOwnerAndName(ownerId, petName);
        if (pet == null) {
            return false;
        }

        pet.setHealthRecord(new HealthRecord(healthStatus));
        return true;
    }

    public boolean providePrescription(int ownerId, String petName, String medicineName, String dosage) {
        Pet pet = findPetByOwnerAndName(ownerId, petName);
        if (pet == null) {
            return false;
        }

        Prescription prescription = new Prescription(nextPrescriptionId(), medicineName, dosage);
        pet.setPrescription(prescription);
        return true;
    }

    private int nextOwnerId() {
        int max = 0;
        for (PetOwner owner : petOwners) {
            if (owner.getId() > max) {
                max = owner.getId();
            }
        }
        return max + 1;
    }

    private int nextVeterinarianId() {
        int max = 0;
        for (Veterinarian vet : veterinarians) {
            if (vet.getId() > max) {
                max = vet.getId();
            }
        }
        return max + 1;
    }

    private int nextPetId() {
        int max = 0;
        for (Pet pet : pets) {
            if (pet.getId() > max) {
                max = pet.getId();
            }
        }
        return max + 1;
    }

    private int nextAppointmentId(Pet pet) {
        int max = 0;
        for (Appointment appointment : pet.getAppointments()) {
            if (appointment.getAppointmentID() > max) {
                max = appointment.getAppointmentID();
            }
        }
        return max + 1;
    }

    private int nextPrescriptionId() {
        int max = 0;
        for (Pet pet : pets) {
            if (pet.getPrescription() != null && pet.getPrescription().getId() > max) {
                max = pet.getPrescription().getId();
            }
        }
        return max + 1;
    }
}