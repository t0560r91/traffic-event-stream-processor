package lab.t056.dataplatform.traffic.component.watermarkgenerator;

import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;

import java.io.Serializable;

// Watermark is used to determine when to close the EventTime based Windows.
// Watermark solves the problem of balancing between
//  - closing window pre-maturely before the unordered (late) events arrive
//  - and waiting forever until all the late events arrive which we can never accurately know when.
// When the emitted watermark exceeds the end time of a window, the window closes (unless additional lateness is allowed).
// ProcessingTime based Windows do NOT need Watermark to determine when to close the window.
// Global Window do not close.

public class CustomWatermarkGenerator implements WatermarkGenerator<VehicleEvent> {

  private long maxDelayMilli;
  private long currentMaxTimestampEpochMilli = 0L;

  public CustomWatermarkGenerator(long maxDelayMilli) {
    this.maxDelayMilli = maxDelayMilli;
  }

  @Override
  public void onEvent(VehicleEvent event, long eventTimestamp, WatermarkOutput output) {
    // It will emit watermark only when the event happened after the current max timestamp.
    if (event.getEventTimestamp().toEpochMilli() > currentMaxTimestampEpochMilli) {
      currentMaxTimestampEpochMilli = event.getEventTimestamp().toEpochMilli();
      output.emitWatermark(new Watermark(currentMaxTimestampEpochMilli - maxDelayMilli));
    }
  }

  @Override
  public void onPeriodicEmit(WatermarkOutput output) {

  }
}
