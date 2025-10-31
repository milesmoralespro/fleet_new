package vehicles;

import exceptions.*;
import interfaces.*;

public class Airplane extends AirVehicle 
implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable{
    private double fuelLevel =0;
    private final int passengerCapacity = 200;
    private int currentPassengers = 0;
    private final double cargoCapacity = 10000;
    private double currentCargo = 0;
    private boolean maintenanceNeeded = false;

    public Airplane(
        String id, 
        String model, 
        double maxSpeed, 
        double maxAltitude
    ) 
    throws InvalidOperationException {
        super(id, model, maxSpeed, maxAltitude);
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
            "Flying at %.2f m for %.2f km, fuel used:%.2f L%n", 
            getMaxAltitude(), 
            distance, 
            consumed);
    }

    @Override
    public double calculateFuelEfficiency(){ 
        return 5.0; 
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
        System.out.println("Airplane " + getId() + " maintenance done.");
    }
}
