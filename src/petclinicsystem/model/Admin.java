package petclinicsystem.model;

public class Admin extends User {

    @Override
    public void showMenu() {
        System.out.println("\n===== Admin Menu =====");
        System.out.println("1. Add Pet Owner");
        System.out.println("2. Remove Pet Owner");
        System.out.println("3. List Pet Owners");
        System.out.println("4. Search Pet Owner by Name");
        System.out.println("5. Add Veterinarian");
        System.out.println("6. Remove Veterinarian");
        System.out.println("7. List Veterinarians");
        System.out.println("8. Search Veterinarian by Name");
        System.out.println("9. Back");
    }
}