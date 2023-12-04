package lab.t056.dataplatform.traffic.operator.transformation;

import lab.t056.dataplatform.traffic.entity.event.PerVehicleTypeTrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.util.UUID;

public class CountBasedWindowedAverageFunction
    extends ProcessWindowFunction<VehicleEvent, PerVehicleTypeTrafficMeterEvent, String, GlobalWindow> {

  // Function is created per partition.
  private Long vehicleCounts;
  private Double maxSpeed;
  private Double minSpeed;
  private Double averageSpeed;
  private Instant lastTimestamp;
  private Instant firstTimestamp;

  @Override
  public void process(String key,
                      Context context,
                      Iterable<VehicleEvent> iterable,
                      Collector<PerVehicleTypeTrafficMeterEvent> collector) {


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
          new PerVehicleTypeTrafficMeterEvent(
              UUID.randomUUID(),
              Instant.now(),
              key,
              vehicleCounts,
              averageSpeed,
              maxSpeed,
              minSpeed,
              null,
              null,
              firstTimestamp,
              lastTimestamp  // this is the event time stamp of the last element from the window.
          ));

    }

  }


