package vehicles;

import exceptions.*;
import interfaces.*;

public class CargoShip extends WaterVehicle 
implements CargoCarrier, Maintainable, FuelConsumable {
    private final double cargoCapacity=50000;
    private double currentCargo=0;
    private boolean maintenanceNeeded = false;
    private double fuelLevel = 0.0; // may be unused if sail

    public CargoShip(
        String id, 
        String model, 
        double maxSpeed, 
        boolean hasSail
    ) throws InvalidOperationException {
        super(id, model, maxSpeed, hasSail);
    }

    @Override
    public void move(double distance) 
    throws InvalidOperationException, 
    InsufficientFuelException {
        if (distance < 0){
            throw new InvalidOperationException("Distance negative");
        } 
        if (!hasSail()) {
            double consumed = consumeFuel(distance);
            addMileage(distance);
            if(getCurrentMileage()>10000){
                maintenanceNeeded=true;
            } 
            System.out.printf(
                "Sailing with engine %.2f km, fuel: %.2f L%n", 
                distance, 
                consumed);
        } else {
            // sail-powered: no fuel used
            addMileage(distance);
            if(getCurrentMileage()>10000){
                maintenanceNeeded=true;
            } 
            System.out.printf(
                "Sailing with wind %.2f km, no fuel used.%n", 
                distance);
        }
    }

    @Override
    public double calculateFuelEfficiency(){
        return hasSail() ? 0.0 : 4.0;
    }

    @Override
    public void loadCargo(double weight) 
    throws OverloadException {
        if (currentCargo + weight > cargoCapacity){
            throw new OverloadException("Cargo overload");
        } 
        currentCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) 
    throws InvalidOperationException {
        if (weight > currentCargo){
            throw new InvalidOperationException("Unload too much");
        } 
        currentCargo -= weight;
    }

    @Override
    public double getCargoCapacity(){ 
        return cargoCapacity; 
    }
    @Override
    public double getCurrentCargo(){ 
        return currentCargo; 
    }

    @Override
    public void scheduleMaintenance(){ 
        maintenanceNeeded=true; 
    }
    @Override
    public boolean needsMaintenance(){ 
        return maintenanceNeeded || getCurrentMileage() > 10000; 
    }
    @Override
    public void performMaintenance(){
        maintenanceNeeded=false;
        System.out.println(
            "CargoShip " + 
            getId() + 
            " maintenance done.");
    }

    // FuelConsumable
    @Override
    public void refuel(double amount) 
    throws InvalidOperationException {
        if (amount <= 0){
            throw new InvalidOperationException("Invalid refuel");
        } 
        fuelLevel += amount;
    }

    @Override
    public double getFuelLevel(){ 
        return fuelLevel; 
    }

    @Override
    public double consumeFuel(double distance) 
    throws InsufficientFuelException {
        if (calculateFuelEfficiency() == 0.0){
            throw new InsufficientFuelException("This ship uses no fuel (sail)");
        } 
        double need = distance / calculateFuelEfficiency();
        if (need > fuelLevel){
            throw new InsufficientFuelException("Not enough fuel");
        } 
        fuelLevel -= need;
        return need;
    }
}
