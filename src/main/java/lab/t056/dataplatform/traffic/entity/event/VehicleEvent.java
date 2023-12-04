package lab.t056.dataplatform.traffic.entity.event;

import lab.t056.dataplatform.traffic.entity.thing.Passenger;
import lab.t056.dataplatform.traffic.entity.thing.Vehicle;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class VehicleEvent implements TrafficEvent, Serializable {
  private UUID eventUUID;
  private Instant eventTimestamp;
  private int speed;
  private Vehicle vehicle;
  private ArrayList<Passenger> passengers;

  public VehicleEvent() {};

  public VehicleEvent(UUID eventUUID,
                      Instant eventTimestamp,
                      int speed,
                      Vehicle vehicle,
                      ArrayList<Passenger> passengers) {

    this.eventUUID = eventUUID;
    this.eventTimestamp = eventTimestamp;
    this.speed = speed;
    this.vehicle = vehicle;
    this.passengers = passengers;
  }

  @Override
  public UUID getEventUUID() {
    return eventUUID;
  }

  @Override
  public void setEventUUID(UUID eventUUID) {
    this.eventUUID = eventUUID;
  }

  @Override
  public Instant getEventTimestamp() {
    return eventTimestamp;
  }

  @Override
  public void setEventTimestamp(Instant eventTimestamp) {
    this.eventTimestamp = eventTimestamp;
  }

  public int getSpeed() {
    return speed;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public Vehicle getVehicle() {
    return vehicle;
  }

  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
  }

  public ArrayList<Passenger> getPassengers() {
    return passengers;
  }

  public void setPassengers(ArrayList<Passenger> passengers) {
    this.passengers = passengers;
  }

  @Override
  public String toString() {
    return "VehicleEvent{" +
        "eventUUID=" + eventUUID +
        ", eventTimestamp=" + eventTimestamp +
        ", speed=" + speed +
        ", vehicle=" + vehicle.getClass().getSimpleName() +
        ", passengers=" + passengers +
        '}';
  }
}
