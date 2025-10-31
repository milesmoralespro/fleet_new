package vehicles;

import exceptions.*;
import interfaces.*;

public class Truck 
extends LandVehicle 
implements FuelConsumable, CargoCarrier, Maintainable {
    private double fuelLevel = 0.0;
    private final double cargoCapacity = 5000.0; // kg
    private double currentCargo = 0.0;
    private boolean maintenanceNeeded = false;

    public Truck(
        String id, 
        String model, 
        double maxSpeed, 
        int wheels
    ) throws InvalidOperationException {
        super(id, model, maxSpeed, wheels);
    }

    @Override
    public void move(double distance) 
    throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0){
            throw new InvalidOperationException("Distance negative");
        } 
        // Adjusted efficiency if loaded > 50% capacity
        double eff = calculateFuelEfficiency();
        double consumed = (distance / eff);
        if (consumed > fuelLevel){
            throw new InsufficientFuelException("Not enough fuel");
        } 
        fuelLevel -= consumed;
        addMileage(distance);
        if(getCurrentMileage()>10000){
            maintenanceNeeded=true;
        } 
        System.out.printf(
            "Hauling cargo %.2f km, fuel consumed:%.2f L%n", 
            distance, 
            consumed);
    }

    @Override
    public double calculateFuelEfficiency() {
        double eff = 8.0;
        if (currentCargo > cargoCapacity * 0.5){
            eff *= 0.9; // reduce by 10%
        } 
        return eff;
    }

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
        double need = distance / calculateFuelEfficiency();
        if (need > fuelLevel){
            throw new InsufficientFuelException("Not enough fuel");
        } 
        fuelLevel -= need;
        return need;
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
            throw new InvalidOperationException("Unloading too much");
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
    public void scheduleMaintenance(){maintenanceNeeded=true; 
    }
    @Override
    public boolean needsMaintenance(){ 
        return maintenanceNeeded || getCurrentMileage() > 10000; 
    }
    @Override
    public void performMaintenance() {
        maintenanceNeeded = false;
        System.out.println("Truck " + getId() + " maintenance done.");
    }
}
