package interfaces;

import exceptions.OverloadException;
import exceptions.InvalidOperationException;

public interface PassengerCarrier {
    void boardPassengers(int count) throws OverloadException;
    void disembarkPassengers(int count) throws InvalidOperationException;
    int getPassengerCapacity();
    int getCurrentPassengers();
}
