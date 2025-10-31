package vehicles;

import exceptions.*;
import interfaces.*;

public class Bus extends LandVehicle 
implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {
    private double fuelLevel = 0.0;
    private final int passengerCapacity = 50;
    private int currentPassengers = 0;
    private final double cargoCapacity=500;
    private double currentCargo=0;
    private boolean maintenanceNeeded = false;

    public Bus(
        String id, 
        String model, 
        double maxSpeed, 
        int wheels
    ) throws InvalidOperationException {
        super(id, model, maxSpeed, wheels);
    }

    @Override
    public void move(double distance) 
    throws InvalidOperationException, 
    InsufficientFuelException {
        if (distance < 0){
            throw new InvalidOperationException("Distance negative");
        } 
        double consumed = consumeFuel(distance);
        addMileage(distance);
        if(getCurrentMileage()>10000){
            maintenanceNeeded=true;
        } 
        System.out.printf(
            "Transporting passengers and cargo %.2f km, fuel used: %.2f L%n", 
            distance, 
            consumed);
    }

    @Override
    public double calculateFuelEfficiency(){ 
        return 10.0; 
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
    public void boardPassengers(int count) 
    throws OverloadException {
        if (currentPassengers + count > passengerCapacity){
            throw new OverloadException("Passenger overload");
        } 
        currentPassengers += count;
    }

    @Override
    public void disembarkPassengers(int count) 
    throws InvalidOperationException {
        if (count > currentPassengers){
            throw new InvalidOperationException("Too many to disembark");
        } 
        currentPassengers -= count;
    }

    @Override
    public int getPassengerCapacity(){ 
        return passengerCapacity; 
    }
    @Override
    public int getCurrentPassengers(){ 
        return currentPassengers; 
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
        if (weight > currentCargo) {
            throw new InvalidOperationException("Unload more than onboard");
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
    public void performMaintenance() {
        maintenanceNeeded = false;
        System.out.println("Bus " + getId() + " maintenance done.");
    }
}
