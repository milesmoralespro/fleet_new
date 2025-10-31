package vehicles;

import exceptions.*;
import interfaces.*;

public class Car extends LandVehicle 
implements FuelConsumable, PassengerCarrier, Maintainable {
    private double fuelLevel = 0.0;
    private final int passengerCapacity = 5;
    private int currentPassengers = 0;
    private boolean maintenanceNeeded = false;

    public Car(
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
            throw new InvalidOperationException("Distance must be >=0");
        } 
        double consumed = consumeFuel(distance);
        addMileage(distance);
        if(getCurrentMileage()>10000){
            maintenanceNeeded=true;
        } 
        System.out.printf(
            "Driving on road for %.2f km, fuel consumed: %.2f L%n", 
            distance, 
            consumed);
    }

    @Override
    public double calculateFuelEfficiency(){ 
        return 15.0; 
    }

    @Override
    public void refuel(double amount) 
    throws InvalidOperationException {
        if (amount <= 0){
            throw new InvalidOperationException("Refuel amount must be > 0");
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
            throw new OverloadException("Too many passengers");
        } 
        currentPassengers += count;
    }

    @Override
    public void disembarkPassengers(int count) 
    throws InvalidOperationException {
        if (count > currentPassengers){
            throw new InvalidOperationException("Cannot disembark more than onboard");
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
        System.out.println("Car " + getId() + " maintenance performed.");
    }
}
