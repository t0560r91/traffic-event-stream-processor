package lab.t056.dataplatform.traffic.entity.event;

import java.time.Instant;
import java.util.UUID;

public class TrafficMeterEvent implements TrafficEvent {
  private UUID eventUUID;
  private Instant eventTimestamp;
  private Long vehicleCounts;
  private Double averageSpeed;
  private Double maxSpeed;
  private Double minSpeed;
  private Instant windowStartTimestamp;
  private Instant windowEndTimestamp;
  private Instant firstTimestamp;
  private Instant lastTimestamp;

  public TrafficMeterEvent() {};

  public TrafficMeterEvent(UUID eventUUID,
                           Instant eventTimestamp,
                           Long vehicleCounts,
                           Double averageSpeed,
                           Double maxSpeed,
                           Double minSpeed,
                           Instant windowStartTimestamp,
                           Instant windowEndTimestamp,
                           Instant firstTimestamp,
                           Instant lastTimestamp
                           ) {

    this.eventUUID = eventUUID;
    this.eventTimestamp = eventTimestamp;
    this.vehicleCounts = vehicleCounts;
    this.averageSpeed = averageSpeed;
    this.maxSpeed = maxSpeed;
    this.minSpeed = minSpeed;
    this.windowStartTimestamp = windowStartTimestamp;
    this.windowEndTimestamp = windowEndTimestamp;
    this.firstTimestamp = firstTimestamp;
    this.lastTimestamp = lastTimestamp;
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

  public Long getVehicleCounts() {
    return vehicleCounts;
  }

  public void setVehicleCounts(Long vehicleCounts) {
    this.vehicleCounts = vehicleCounts;
  }

  public Double getAverageSpeed() {
    return averageSpeed;
  }

  public void setAverageSpeed(Double averageSpeed) {
    this.averageSpeed = averageSpeed;
  }

  public Double getMaxSpeed() {
    return maxSpeed;
  }

  public void setMaxSpeed(Double maxSpeed) {
    this.maxSpeed = maxSpeed;
  }

  public Double getMinSpeed() {
    return minSpeed;
  }

  public void setMinSpeed(Double minSpeed) {
    this.minSpeed = minSpeed;
  }

  public Instant getWindowStartTimestamp() {
    return windowStartTimestamp;
  }

  public void setWindowStartTimestamp(Instant windowStartTimestamp) {
    this.windowStartTimestamp = windowStartTimestamp;
  }

  public Instant getWindowEndTimestamp() {
    return windowEndTimestamp;
  }

  public void setWindowEndTimestamp(Instant windowEndTimestamp) {
    this.windowEndTimestamp = windowEndTimestamp;
  }

  public Instant getFirstTimestamp() {
    return firstTimestamp;
  }

  public void setFirstTimestamp(Instant firstTimestamp) {
    this.firstTimestamp = firstTimestamp;
  }

  public Instant getLastTimestamp() {
    return lastTimestamp;
  }

  public void setLastTimestamp(Instant lastTimestamp) {
    this.lastTimestamp = lastTimestamp;
  }

  @Override
  public String toString() {
    return "TrafficMeterEvent{" +
        "eventUUID=" + eventUUID +
        ", eventTimestamp=" + eventTimestamp +
        ", vehicleCounts=" + vehicleCounts +
        ", averageSpeed=" + averageSpeed +
        ", maxSpeed=" + maxSpeed +
        ", minSpeed=" + minSpeed +
        ", windowStartTimestamp=" + windowStartTimestamp +
        ", windowEndTimestamp=" + windowEndTimestamp +
        ", firstTimestamp=" + firstTimestamp +
        ", lastTimestamp=" + lastTimestamp +
        '}';
  }
}
