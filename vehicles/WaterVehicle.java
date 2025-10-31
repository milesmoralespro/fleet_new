package vehicles;

import exceptions.InvalidOperationException;

public abstract class WaterVehicle 
extends Vehicle {
    private boolean hasSail;

    public WaterVehicle(
        String id, 
        String model, 
        double maxSpeed, 
        boolean hasSail
    ) throws InvalidOperationException {
        super(id, model, maxSpeed);
        this.hasSail = hasSail;
    }

    @Override
    public double estimateJourneyTime(double distance) {
        double base = distance / getMaxSpeed();
        return base * 1.15; // add 15% for currents
    }

    public boolean hasSail(){ 
        return hasSail; 
    }
}
