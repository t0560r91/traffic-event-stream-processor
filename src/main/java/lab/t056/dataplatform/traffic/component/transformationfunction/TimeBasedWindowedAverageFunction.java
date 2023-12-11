package lab.t056.dataplatform.traffic.component.transformationfunction;

import lab.t056.dataplatform.traffic.entity.event.TrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.util.UUID;

// WindowFunction is deprecated in favor of ProcessWindowFunction.
public class TimeBasedWindowedAverageFunction
    implements AllWindowFunction<VehicleEvent, TrafficMeterEvent, TimeWindow> {

  // Function is created per partition.
  private Long vehicleCounts;
  private Double maxSpeed;
  private Double minSpeed;
  private Double averageSpeed;
  private Instant lastTimestamp;
  private Instant firstTimestamp;

  @Override
  public void apply(TimeWindow timeWindow,
                    Iterable<VehicleEvent> iterable,
                    Collector<TrafficMeterEvent> collector) throws Exception {

    // apply method is called per Window.

    vehicleCounts = 0L;
    maxSpeed = Double.MIN_VALUE;
    minSpeed = Double.MAX_VALUE;
    averageSpeed = null;
    lastTimestamp = null;
    firstTimestamp = null;

    iterable.forEach(e -> {

      if (lastTimestamp == null) {
        lastTimestamp = e.getEventTimestamp();
      } else {
        lastTimestamp = lastTimestamp.isBefore(e.getEventTimestamp()) ? e.getEventTimestamp() : lastTimestamp;
      }

      if (firstTimestamp == null) {
        firstTimestamp = e.getEventTimestamp();
      } else {
        firstTimestamp = firstTimestamp.isAfter(e.getEventTimestamp()) ? e.getEventTimestamp() : firstTimestamp;
      }

      if (averageSpeed == null) {
        averageSpeed = (double) e.getSpeed();
      } else {
        averageSpeed = ((averageSpeed * vehicleCounts) + e.getSpeed()) / (vehicleCounts + 1);
      }

      vehicleCounts += 1;
      maxSpeed = Math.max(maxSpeed, e.getSpeed());
      minSpeed = Math.min(minSpeed, e.getSpeed());
    });

    collector.collect(
        new TrafficMeterEvent(
            UUID.randomUUID(),
            Instant.now(),
            vehicleCounts,
            averageSpeed,
            maxSpeed,
            minSpeed,
            Instant.ofEpochMilli(timeWindow.getStart()),
            Instant.ofEpochMilli(timeWindow.getEnd()),
            firstTimestamp,
            lastTimestamp
        ));

  }
}
