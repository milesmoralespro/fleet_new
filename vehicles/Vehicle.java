package vehicles;

import exceptions.InvalidOperationException;

public abstract class Vehicle 
implements Comparable<Vehicle> {
    private String id;
    private String model;
    private double maxSpeed;
    private double currentMileage;

    public Vehicle(
        String id, 
        String model, 
        double maxSpeed
    ) throws InvalidOperationException {
        if(id==null||id.trim().isEmpty()) {
            throw new InvalidOperationException("ID cannot be empty");
        }
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = 0.0;
    }

    public abstract void move(double distance) 
    throws InvalidOperationException, 
    Exception;

    public abstract double calculateFuelEfficiency();

    public abstract double estimateJourneyTime(double distance);

    public void displayInfo(){
        System.out.printf(
            "ID:%s | Model:%s | MaxSpeed:%.2f | Mileage:%.2f%n",
            id, 
            model, 
            maxSpeed, 
            currentMileage);
    }

    protected void addMileage(double d){
        this.currentMileage+=d;
    }

    public double getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(double mileage) {
        this.currentMileage = mileage;
    }

    public String getId() {
        return id;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public String getModel() {
        return model;
    }

    @Override
    public int compareTo(Vehicle other) {
        // higher efficiency sorts first
        return Double.compare(
            other.calculateFuelEfficiency(), 
            this.calculateFuelEfficiency()
        );
    }
}
