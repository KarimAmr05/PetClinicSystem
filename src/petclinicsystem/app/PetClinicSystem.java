package petclinicsystem.app;

import petclinicsystem.model.Admin;
import petclinicsystem.model.Appointment;
import petclinicsystem.model.Pet;
import petclinicsystem.model.PetOwner;
import petclinicsystem.model.Prescription;
import petclinicsystem.model.Veterinarian;
import petclinicsystem.service.ClinicService;
import petclinicsystem.util.InputHelper;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class PetClinicSystem {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ClinicService clinic = new ClinicService();
        clinic.loadAll();

        Admin adminMenu = new Admin();
        PetOwner petOwnerMenu = new PetOwner();
        Veterinarian veterinarianMenu = new Veterinarian();

        boolean running = true;

        while (running) {
            System.out.println("\n===== PetClinicSystem =====");
            System.out.println("1. Admin");
            System.out.println("2. Pet Owner");
            System.out.println("3. Veterinarian");
            System.out.println("4. Save and Exit");

            int mainChoice = InputHelper.readInt(input, "Enter your choice: ");

            switch (mainChoice) {
                case 1:
                    runAdminMenu(input, clinic, adminMenu);
                    break;

                case 2:
                    runPetOwnerMenu(input, clinic, petOwnerMenu);
                    break;

                case 3:
                    runVeterinarianMenu(input, clinic, veterinarianMenu);
                    break;

                case 4:
                    clinic.saveAll();
                    System.out.println("Data saved. Exiting...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        input.close();
    }

    private static void runAdminMenu(Scanner input, ClinicService clinic, Admin adminMenu) {
        boolean back = false;

        while (!back) {
            adminMenu.showMenu();
            int choice = InputHelper.readInt(input, "Enter your choice: ");

            switch (choice) {
                case 1: {
                    String name = InputHelper.readString(input, "Enter Pet Owner Name: ");
                    String email = InputHelper.readString(input, "Enter Pet Owner Email: ");
                    PetOwner owner = clinic.addPetOwner(name, email);
                    clinic.saveAll();
                    System.out.println("Pet Owner added successfully. ID: " + owner.getId());
                    break;
                }

                case 2: {
                    int ownerId = InputHelper.readInt(input, "Enter Pet Owner ID to remove: ");
                    boolean removed = clinic.removePetOwner(ownerId);
                    if (removed) {
                        clinic.saveAll();
                        System.out.println("Pet Owner removed successfully.");
                    } else {
                        System.out.println("Pet Owner not found.");
                    }
                    break;
                }

                case 3: {
                    List<PetOwner> owners = clinic.getPetOwners();
                    if (owners.isEmpty()) {
                        System.out.println("No pet owners found.");
                    } else {
                        for (PetOwner owner : owners) {
                            System.out.println(owner);
                            if (owner.getPets().isEmpty()) {
                                System.out.println("   Pets: none");
                            } else {
                                for (Pet pet : owner.getPets()) {
                                    System.out.println("   " + pet);
                                }
                            }
                        }
                    }
                    break;
                }

                case 4: {
                    String ownerName = InputHelper.readString(input, "Enter Pet Owner Name to search: ");
                    PetOwner owner = clinic.findPetOwnerByName(ownerName);
                    if (owner != null) {
                        System.out.println("Pet Owner found:");
                        System.out.println(owner);
                    } else {
                        System.out.println("Pet Owner not found.");
                    }
                    break;
                }

                case 5: {
                    String vetName = InputHelper.readString(input, "Enter Veterinarian Name: ");
                    Veterinarian vet = clinic.addVeterinarian(vetName);
                    clinic.saveAll();
                    System.out.println("Veterinarian added successfully. ID: " + vet.getId());
                    break;
                }

                case 6: {
                    int vetId = InputHelper.readInt(input, "Enter Veterinarian ID to remove: ");
                    boolean removed = clinic.removeVeterinarian(vetId);
                    if (removed) {
                        clinic.saveAll();
                        System.out.println("Veterinarian removed successfully.");
                    } else {
                        System.out.println("Veterinarian not found.");
                    }
                    break;
                }

                case 7: {
                    List<Veterinarian> vets = clinic.getVeterinarians();
                    if (vets.isEmpty()) {
                        System.out.println("No veterinarians found.");
                    } else {
                        for (Veterinarian vet : vets) {
                            System.out.println(vet);
                        }
                    }
                    break;
                }

                case 8: {
                    String vetName = InputHelper.readString(input, "Enter Veterinarian Name to search: ");
                    Veterinarian vet = clinic.findVeterinarianByName(vetName);
                    if (vet != null) {
                        System.out.println("Veterinarian found:");
                        System.out.println(vet);
                    } else {
                        System.out.println("Veterinarian not found.");
                    }
                    break;
                }

                case 9:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void runPetOwnerMenu(Scanner input, ClinicService clinic, PetOwner petOwnerMenu) {
        int ownerId = InputHelper.readInt(input, "Enter your Pet Owner ID: ");
        PetOwner owner = clinic.findPetOwnerById(ownerId);

        if (owner == null) {
            System.out.println("Pet Owner not found.");
            return;
        }

        System.out.println("Welcome, " + owner.getName());

        boolean back = false;

        while (!back) {
            petOwnerMenu.showMenu();
            int choice = InputHelper.readInt(input, "Enter your choice: ");

            switch (choice) {
                case 1: {
                    String petName = InputHelper.readString(input, "Enter Pet Name: ");
                    String breed = InputHelper.readString(input, "Enter Pet Breed: ");
                    int age = InputHelper.readInt(input, "Enter Pet Age: ");

                    Pet pet = clinic.addPetToOwner(ownerId, petName, breed, age);
                    if (pet != null) {
                        clinic.saveAll();
                        System.out.println("Pet added successfully. Pet ID: " + pet.getId());
                    } else {
                        System.out.println("Could not add pet.");
                    }
                    break;
                }

                case 2: {
                    String petName = InputHelper.readString(input, "Enter Pet Name: ");
                    int vetId = InputHelper.readInt(input, "Enter Veterinarian ID: ");
                    java.time.LocalDateTime dateTime = InputHelper.readDateTime(
                            input,
                            "Enter Appointment Date and Time (yyyy-MM-dd HH:mm): "
                    );

                    Appointment appointment = clinic.bookAppointment(ownerId, petName, vetId, dateTime);
                    if (appointment != null) {
                        clinic.saveAll();
                        System.out.println("Appointment booked successfully:");
                        System.out.println(appointment);
                    } else {
                        System.out.println("Could not book appointment. Check pet name or veterinarian ID.");
                    }
                    break;
                }

                case 3: {
                    String petName = InputHelper.readString(input, "Enter Pet Name: ");
                    Pet pet = clinic.findPetByOwnerAndName(ownerId, petName);

                    if (pet == null) {
                        System.out.println("Pet not found.");
                    } else {
                        printPetDetails(pet);
                    }
                    break;
                }

                case 4: {
                    List<Pet> pets = clinic.getPetsForOwner(ownerId);
                    if (pets.isEmpty()) {
                        System.out.println("You have no pets.");
                    } else {
                        for (Pet pet : pets) {
                            System.out.println(pet);
                        }
                    }
                    break;
                }

                case 5:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void runVeterinarianMenu(Scanner input, ClinicService clinic, Veterinarian veterinarianMenu) {
        int vetId = InputHelper.readInt(input, "Enter your Veterinarian ID: ");
        Veterinarian vet = clinic.findVeterinarianById(vetId);

        if (vet == null) {
            System.out.println("Veterinarian not found.");
            return;
        }

        System.out.println("Welcome, Dr. " + vet.getName());

        boolean back = false;

        while (!back) {
            veterinarianMenu.showMenu();
            int choice = InputHelper.readInt(input, "Enter your choice: ");

            switch (choice) {
                case 1: {
                    int ownerId = InputHelper.readInt(input, "Enter Pet Owner ID: ");
                    String petName = InputHelper.readString(input, "Enter Pet Name: ");
                    String healthStatus = InputHelper.readString(input, "Enter New Health Record: ");

                    boolean updated = clinic.updateHealthRecord(ownerId, petName, healthStatus);
                    if (updated) {
                        clinic.saveAll();
                        System.out.println("Health record updated successfully.");
                    } else {
                        System.out.println("Pet not found.");
                    }
                    break;
                }

                case 2: {
                    int ownerId = InputHelper.readInt(input, "Enter Pet Owner ID: ");
                    String petName = InputHelper.readString(input, "Enter Pet Name: ");
                    String medicineName = InputHelper.readString(input, "Enter Medicine Name: ");
                    String dosage = InputHelper.readString(input, "Enter Dosage: ");

                    boolean created = clinic.providePrescription(ownerId, petName, medicineName, dosage);
                    if (created) {
                        clinic.saveAll();
                        Pet pet = clinic.findPetByOwnerAndName(ownerId, petName);
                        System.out.println("Prescription added successfully.");
                        System.out.println(pet.getPrescription());
                    } else {
                        System.out.println("Pet not found.");
                    }
                    break;
                }

                case 3: {
                    int ownerId = InputHelper.readInt(input, "Enter Pet Owner ID: ");
                    String petName = InputHelper.readString(input, "Enter Pet Name: ");
                    Pet pet = clinic.findPetByOwnerAndName(ownerId, petName);

                    if (pet == null) {
                        System.out.println("Pet not found.");
                    } else {
                        printPetDetails(pet);
                    }
                    break;
                }

                case 4:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void printPetDetails(Pet pet) {
        System.out.println("\n===== Pet Details =====");
        System.out.println(pet);
        System.out.println("Health Record: " + pet.getHealthRecord());

        Prescription prescription = pet.getPrescription();
        if (prescription == null || prescription.getMedicineName() == null || prescription.getMedicineName().isBlank()) {
            System.out.println("Prescription: None");
        } else {
            System.out.println("Prescription: " + prescription);
        }

        if (pet.getAppointments().isEmpty()) {
            System.out.println("Appointments: None");
        } else {
            System.out.println("Appointments:");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Appointment appointment : pet.getAppointments()) {
                System.out.println(" - Appointment ID: " + appointment.getAppointmentID()
                        + ", Date/Time: " + appointment.getDateTime().format(formatter)
                        + ", Status: " + appointment.getStatus()
                        + ", Vet: " + appointment.getVetName()
                        + " (ID: " + appointment.getVetId() + ")");
            }
        }
    }
}