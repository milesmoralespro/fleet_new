package vehicles;

import exceptions.InvalidOperationException;

public abstract class AirVehicle extends Vehicle{
    private double maxAltitude;

    public AirVehicle(
        String id, 
        String model, 
        double maxSpeed, 
        double maxAltitude
    ) throws InvalidOperationException{
        super(id, model, maxSpeed);
        this.maxAltitude = maxAltitude;
    }

    @Override
    public double estimateJourneyTime(double distance){
        double base = distance / getMaxSpeed();
        return base * 0.95; // reduce 5% for direct paths
    }

    public double getMaxAltitude(){ 
        return maxAltitude; 
    }
}
