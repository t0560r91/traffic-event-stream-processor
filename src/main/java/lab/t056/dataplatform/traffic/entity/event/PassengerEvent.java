package lab.t056.dataplatform.traffic.entity.event;

import lab.t056.dataplatform.traffic.entity.thing.Passenger;
import lab.t056.dataplatform.traffic.entity.thing.Vehicle;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class PassengerEvent implements Serializable {
  // TODO: Figure out if Serializable marker is required?
  private UUID eventUUID;
  private Instant eventTimestamp;
  private Passenger passenger;
  private Vehicle vehicle;
  private UUID vehicleEventUUID;

  public PassengerEvent() {};

  public PassengerEvent(UUID eventUUID,
                        Instant eventTimestamp,
                        Passenger passenger,
                        Vehicle vehicle,
                        UUID vehicleEventUUID) {

    this.eventUUID = eventUUID;
    this.eventTimestamp = eventTimestamp;
    this.passenger = passenger;
    this.vehicle = vehicle;
    this.vehicleEventUUID = vehicleEventUUID;
  }

  public UUID getEventUUID() {
    return eventUUID;
  }

  public void setEventUUID(UUID eventUUID) {
    this.eventUUID = eventUUID;
  }

  public Instant getEventTimestamp() {
    return eventTimestamp;
  }

  public void setEventTimestamp(Instant eventTimestamp) {
    this.eventTimestamp = eventTimestamp;
  }

  public Passenger getPassenger() {
    return passenger;
  }

  public void setPassenger(Passenger passenger) {
    this.passenger = passenger;
  }

  public Vehicle getVehicle() {
    return vehicle;
  }

  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
  }

  public UUID getVehicleEventUUID() {
    return vehicleEventUUID;
  }

  public void setVehicleEventUUID(UUID vehicleEventUUID) {
    this.vehicleEventUUID = vehicleEventUUID;
  }

  @Override
  public String toString() {
    return "PassengerEvent{" +
        "eventUUID=" + eventUUID +
        ", eventTimestamp=" + eventTimestamp +
        ", passenger=" + passenger +
        ", vehicle=" + vehicle +
        ", vehicleEventUUID=" + vehicleEventUUID +
        '}';
  }
}
