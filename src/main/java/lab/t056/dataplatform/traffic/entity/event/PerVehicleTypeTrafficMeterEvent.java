package lab.t056.dataplatform.traffic.entity.event;

import java.time.Instant;
import java.util.UUID;

public class PerVehicleTypeTrafficMeterEvent extends TrafficMeterEvent  {
  private String vehicleType;

  public PerVehicleTypeTrafficMeterEvent() {};

  public PerVehicleTypeTrafficMeterEvent(UUID eventUUID,
                                         Instant eventTimestamp,
                                         String vehicleType,
                                         Long vehicleCounts,
                                         Double averageSpeed,
                                         Double maxSpeed,
                                         Double minSpeed,
                                         Instant windowStartTimestamp,
                                         Instant windowEndTimestamp,
                                         Instant firstTimestamp,
                                         Instant lastTimestamp
                           ) {

    super(
        eventUUID,
        eventTimestamp,
        vehicleCounts,
        averageSpeed,
        maxSpeed,
        minSpeed,
        windowStartTimestamp,
        windowEndTimestamp,
        firstTimestamp,
        lastTimestamp
    );
    this.vehicleType = vehicleType;
  }

  public String getVehicleType() {
    return vehicleType;
  }

  public void setVehicleType(String vehicleType) {
    this.vehicleType = vehicleType;
  }

  @Override
  public String toString() {
    return "PerVehicleTypeTrafficMeterEvent{" +
        "eventUUID=" + getEventUUID() +
        ", eventTimestamp=" + getEventTimestamp() +
        ", vehicleType='" + vehicleType + '\'' +
        ", vehicleCounts=" + getVehicleCounts() +
        ", averageSpeed=" + getAverageSpeed() +
        ", maxSpeed=" + getMaxSpeed() +
        ", minSpeed=" + getMinSpeed() +
        ", windowStartTimestamp=" + getWindowStartTimestamp() +
        ", windowEndTimestamp=" + getWindowEndTimestamp() +
        ", firstTimestamp=" + getFirstTimestamp() +
        ", lastTimestamp=" + getLastTimestamp() +
        '}';
  }
}
