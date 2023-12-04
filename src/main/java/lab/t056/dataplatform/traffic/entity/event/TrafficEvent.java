package lab.t056.dataplatform.traffic.entity.event;

import java.time.Instant;
import java.util.UUID;

public interface TrafficEvent {
  void setEventUUID(UUID eventUUID);

  void setEventTimestamp(Instant eventTimestamp);

  UUID getEventUUID();

  Instant getEventTimestamp();

}
