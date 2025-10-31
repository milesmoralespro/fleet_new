package fleet;

import vehicles.*;
import interfaces.*;

public class VehicleFactory {
    /**
     * Creates a vehicle from input in form of .CSV format which is split into parts.
     * Format (saved by FleetManager):
     * Type,id,model,maxSpeed,mileage[,FUEL,x][,CARGO,y,CAP,z][,PAX,y,PCAP,z]
     */
    public static Vehicle createFromCsv (String[] parts) 
    throws Exception {
        if (parts.length < 5){
            throw new IllegalArgumentException("Incorrect CSV format");
        }
        String type = parts[0];
        String id = parts[1];
        String model = parts[2];
        double maxSpeed = Double.parseDouble(parts[3]);
        double mileage = Double.parseDouble(parts[4]);

        Vehicle v;
        switch (type) {
            case "Car":
                v = new Car(id, model, maxSpeed, 4);
                break;
            case "Truck":
                v = new Truck(id, model, maxSpeed, 6);
                break;
            case "Bus":
                v = new Bus(id, model, maxSpeed, 6);
                break;
            case "Airplane":
                v = new Airplane(id,model,maxSpeed,15240.0);
                break;
            case "CargoShip":
                v = new CargoShip(id, model, maxSpeed, false);
                break;
            default:
                throw new IllegalArgumentException("Unknown type:" + type);
        }

        if (mileage>0) {
            v.setCurrentMileage(mileage);
        }

        // Parse remaining key-value pairs
        int size = parts.length; 
        for(int i=5;i<size-1;i++){
            String key = parts[i];
            String val = parts[i + 1];
            switch (key) {
                case "FUEL":
                    if (v instanceof FuelConsumable) {
                        double fuel = Double.parseDouble(val);
                        ((FuelConsumable) v).refuel(fuel);
                    }
                    break;
                case "CARGO":
                    if (v instanceof CargoCarrier) {
                        double cargo = Double.parseDouble(val);
                        if (cargo > 0) ((CargoCarrier) v).loadCargo(cargo);
                    }
                    break;
                case "PAX":
                    if (v instanceof PassengerCarrier pc) {
                        int p = Integer.parseInt(val);
                        if (p > 0) pc.boardPassengers(p);
                    }
                    break;
                default:
                    // ignore CAP, PCAP, etc. (capacity is constant in constructors)
                    break;
            }
        }

        return v;
    }
}
