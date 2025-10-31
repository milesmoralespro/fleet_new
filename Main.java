import fleet.FleetManager;
import vehicles.*;
import exceptions.*;

import java.util.*;

public class Main {
    private static FleetManager manager=new FleetManager();
    private static Scanner sc=new Scanner(
        System.in
    );

    public static void main(String[]args){
        try {
            // demo vehicles
            Car car = new Car(
                "C001", 
                "Toyota", 
                120.0, 
                4
            );
            car.refuel(50.0);
            
            Truck truck = new Truck(
                "T001", 
                "Volvo", 
                100.0, 
                6
            );
            truck.refuel(100.0);
            
            Bus bus = new Bus(
                "B001", 
                "Mercedes", 
                90.0, 
                6
            );
            bus.refuel(80.0);
            
            Airplane airplane=new Airplane(
                "A001", 
                "Boeing", 
                800.0, 
                12000.0
            );
            airplane.refuel(1000.0);
            
            CargoShip cargoShip=new CargoShip(
                "S001", 
                "Maersk", 
                40.0, 
                false
            );
            cargoShip.refuel(1200.0);

            manager.addVehicle(car);
            manager.addVehicle(truck);
            manager.addVehicle(bus);
            manager.addVehicle(airplane);
            manager.addVehicle(cargoShip);
        } 
        catch(Exception e){
            System.out.println(
                "Setup error: " 
                + e.getMessage()
            );
        }

        System.out.println(
            "Demo: starting 100 km journey..."
        );
        manager.startAllJourneys(100.0);
        System.out.println(manager.generateReport());

        // CLI loop
        while (true) {
            showMenu();
            String choice = sc.nextLine().trim();
            try {
                if (choice.equals("1")) addVehicleCLI();
                else if (choice.equals("2")) removeVehicleCLI();
                else if (choice.equals("3")) startJourneyCLI();
                else if (choice.equals("4")) refuelAllCLI();
                else if (choice.equals("5")) manager.maintainAll();
                else if (choice.equals("6")) System.out.println(manager.generateReport());
                else if (choice.equals("7")) saveCLI();
                else if (choice.equals("8")) loadCLI();
                else if (choice.equals("9")) searchCLI();
                else if (choice.equals("10")) listMaint();
                else if (choice.equals("11")) sortingMenu();
                else if (choice.equals("12")) displayFastestSlowest();
                else if (choice.equals("13")) displayMostLeastEfficient();
                else if (choice.equals("14")) manager.displayUniqueModels();
                else if (choice.equals("15")) manager.displaySortedByEfficiency();
                else if (choice.equals("16")) displayAllVehicles();
                else if (choice.equals("17")){ 
                    System.out.println(
                        "Exiting... " 
                        + "Goodbye!"
                    ); 
                    break; 
                }
                else {
                    System.out.println("Invalid option");
                } 
            } 
            catch(Exception e){
                System.out.println(
                    "Error: " 
                    + e.getMessage()
                );
            } finally {
                sc.close();
            }
        }
    }

    private static void showMenu(){
        System.out.println("\nMenu:");
        System.out.println("1. Add Vehicle");
        System.out.println("2. Remove Vehicle");
        System.out.println("3. Start Journey");
        System.out.println("4. Refuel All");
        System.out.println("5. Perform Maintenance");
        System.out.println("6. Generate Report");
        System.out.println("7. Save Fleet");
        System.out.println("8. Load Fleet");
        System.out.println("9. Search by Type");
        System.out.println("10. List Vehicles Needing Maintenance");
        System.out.println("11. Sort Fleet (Submenu)");
        System.out.println("12. Show Fastest/Slowest Vehicle");
        System.out.println("13. Show Most/Least Efficient Vehicle");
        System.out.println("14. Display Unique Models (HashSet)");
        System.out.println("15. Display Sorted by Efficiency (TreeSet)");
        System.out.println("16. Display All Vehicles");
        System.out.println("17. Exit");
        System.out.print(
            "Enter" 
            + "your Choice:"
        );
    }

    private static void sortingMenu() {
        System.out.println(
            "\n=== SORTING" 
            + " OPTIONS ==="
        );
        System.out.println(
            "1. Sort by " 
            +"Fuel Efficiency"
        );
        System.out.println(
            "2. Sort by "
            +"Maximum Speed"
        );
        System.out.println(
            "3. Sort by "
            +"Model Name"
        );
        System.out.println(
            "4. Sort by "
            +"Vehicle ID"
        );
        System.out.print(
            "Enter choice: "
        );
        
        String choice = sc.nextLine()
                        .trim();
        
        switch (choice) {
            case "1":
                manager.sortFleetByEfficiency();
                displayAllVehicles();
                break;
            case "2":
                manager.sortFleetBySpeed();
                displayAllVehicles();
                break;
            case "3":
                manager.sortFleetByModel();
                displayAllVehicles();
                break;
            case "4":
                manager.sortFleetById();
                displayAllVehicles();
                break;
            default:
                System.out.println(
                    "Invalid "
                    +"sorting option."
                );
        }
    }

    private static void displayFastestSlowest() {
        System.out.println(
            "\n=== Fastest"
            +" and Slowest Vehicles ==="
        );
        
        Vehicle fastest = manager.getFastestVehicle();
        Vehicle slowest = manager.getSlowestVehicle();
        
        if(fastest!=null){
            System.out.println("\nFastest Vehicle:");
            fastest.displayInfo();
            System.out.printf(
                "Max Speed: %.2f km/h%n", 
                fastest.getMaxSpeed()
            );
        } else {
            System.out.println(
                "No vehicles in fleet."
            );
        }
        
        if (slowest != null) {
            System.out.println(
                "\nSlowest Vehicle:"
            );
            slowest.displayInfo();
            System.out.printf(
                "Max Speed: %.2f km/h%n", 
                slowest.getMaxSpeed()
            );
        }
    }

    private static void displayMostLeastEfficient() {
        System.out.println(
            "\n=== Most and"
            +" Least Efficient Vehicles ==="
        );
        
        Vehicle mostEff = manager.getMostEfficientVehicle();
        Vehicle leastEff = manager.getLeastEfficientVehicle();
        
        if(mostEff != null){
            System.out.println(
                "\nMost Efficient Vehicle:"
            );
            mostEff.displayInfo();
            System.out.printf(
                "Fuel Efficiency: %.2f km/L%n", 
                mostEff.calculateFuelEfficiency()
            );
        } else {
            System.out.println(
                "No vehicles in fleet."
            );
        }
        
        if (
            leastEff != null && 
            leastEff.calculateFuelEfficiency() > 0
        ) {
            System.out.println(
                "\nLeast Efficient Vehicle:"
            );
            leastEff.displayInfo();
            System.out.printf(
                "Fuel Efficiency: %.2f km/L%n", 
                leastEff.calculateFuelEfficiency()
            );
        }
    }

    private static void displayAllVehicles() {
        System.out.println(
            "\n=== All Vehicles"
            +" in Fleet ==="
        );
        List<Vehicle> vehicles = manager.getFleets();
        
        if (vehicles.isEmpty()) {
            System.out.println(
                "No vehicles in fleet."
            );
            return;
        }
        
        System.out.println(
            "Total vehicles: " 
            + vehicles.size()
        );
        System.out.println(
            "----------------------------------------"
        );
        
        int count = 1;
        for (Vehicle v : vehicles) {
            System.out.print(
                count++ 
                + ". "
            );
            v.displayInfo();
            System.out.printf(
                "   Type: %s | Efficiency:"
                +" %.2f km/L%n",
                v.getClass().getSimpleName(),
                v.calculateFuelEfficiency());
        }
    }

    private static void addVehicleCLI() 
    throws InvalidOperationException {
        System.out.print(
            "Type (Car/Truck" 
            + "/Bus/Airplane" 
            + "/CargoShip): "
        );
        String t = sc
        .nextLine()
        .trim();
        System.out.print(
            "ID: "
        );
        String id = sc
        .nextLine()
        .trim();
        System.out.print(
            "Model: "
        );
        String model = sc
        .nextLine()
        .trim();
        System.out.print(
            "Max speed: "
        );
        double ms = Double.parseDouble(
            sc.nextLine()
            .trim()
        );
        Vehicle v = null;
        switch (t) {
            case "Car": 
                v = new Car(id, model, ms, 4); 
                break;
            case "Truck": 
                v = new Truck(id, model, ms, 6); 
                break;
            case "Bus": 
                v = new Bus(id, model, ms, 6); 
                break;
            case "Airplane": 
                v = new Airplane(id, model, ms, 10000.0); 
                break;
            case "CargoShip": 
                v = new CargoShip(id, model, ms, false); 
                break;
            default: 
                System.out.println(
                    "Unknown type"
                ); 
                return;
        }
        manager.addVehicle(v);
        System.out.println(
            "Added " 
            + id
        );
    }

    private static void removeVehicleCLI() throws InvalidOperationException {
        System.out.print(
            "ID to remove: "
        );
        String id = sc
        .nextLine()
        .trim();
        manager.removeVehicle(id);
        System.out.println(
            "Removed "
            + id
        );
    }

    private static void startJourneyCLI() {
        System.out.print(
            "Distance (km): "
        );
        double d = Double.parseDouble(
            sc.nextLine()
            .trim()
        );
        manager.startAllJourneys(d);
    }

    private static void refuelAllCLI() {
        System.out.print(
            "Refuel amount (each fuel vehicle): ");
        double amt = Double.parseDouble(
            sc.nextLine()
            .trim()
        );
        for(Vehicle v:manager.getFleets()){
            if (v instanceof interfaces.FuelConsumable) {
                try {
                    ((interfaces.FuelConsumable) v)
                    .refuel(amt);
                } 
                catch(Exception e){
                    System.out.println(
                        "Refuel failed for "
                         + v.getId()
                         + ": "
                         + e.getMessage()
                    );
                }
            }
        }
    }

    private static void saveCLI() {
        System.out.print(
            "Filename: ");
        String f = sc
        .nextLine()
        .trim();
        try { 
            manager.saveFile(f); 
            System.out.println(
                "Saved to "
                 + f); 
        }
        catch(Exception e){ 
            System.out.println(
                "Save failed: " 
                + e.getMessage()); 
        }
    }

    private static void loadCLI() {
        System.out.print(
            "Filename: ");
        String f = sc
        .nextLine()
        .trim();
        try { 
            manager.loadFile(f); 
            System.out.println("Loaded from " + f); 
        }
        catch(Exception e){ 
            System.out.println(
                "Load failed: "
                 + e.getMessage()); 
        }
    }

    private static void searchCLI() {
        System.out.print(
            "Type name to search (Car/Truck/...): ");
        String t = sc
        .nextLine()
        .trim();
        try {
            Class<?>cls=Class.forName(
                "vehicles." + t
            );
            var list = manager.searchByType(cls);
            if (list.isEmpty()) {
                System.out.println(
                    "No vehicles of type " 
                    + t 
                    + " found."
                );
            } else {
                System.out.println(
                    "\n=== Found " 
                    + list.size() 
                    + " " 
                    + t 
                    + "(s) ==="
                );
                for (var v : list) {
                    v.displayInfo();
                }
            }
        }
        catch(ClassNotFoundException e){
            System.out.println(
                "Type not found"
            );
        }
    }

    private static void listMaint(){
        var list = manager.getVehiclesNeedingMaintenance();
        if (list.isEmpty()) {
            System.out.println(
                "No vehicles need maintenance."
            );
        }
        else {
            System.out.println(
                "\n=== Vehicles Needing"
                +" Maintenance ==="
            );
            System.out.println(
                "Total: " 
                + list.size()
            );
            for (var v : list) {
                v.displayInfo();
            }
        } 
    }
}
