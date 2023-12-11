package lab.t056.dataplatform.traffic.component.timestampassigner;

import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;

public class VehicleEventTimestampAssigner implements SerializableTimestampAssigner<VehicleEvent> {
  // TODO: Figure out why SerializableTimestampAssigner?
  @Override
  public long extractTimestamp(VehicleEvent e, long recordTimestamp) {
    return e.getEventTimestamp().toEpochMilli();
  }
}
