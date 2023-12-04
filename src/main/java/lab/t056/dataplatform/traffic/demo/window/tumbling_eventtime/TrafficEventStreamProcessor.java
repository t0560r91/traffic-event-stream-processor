package lab.t056.dataplatform.traffic.demo.window.tumbling_eventtime;

import lab.t056.dataplatform.traffic.entity.event.TrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.operator.timestampassigner.VehicleEventTimestampAssigner;
import lab.t056.dataplatform.traffic.operator.transformation.TimeBasedWindowedAverageFunction;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.time.Duration;


public class TrafficEventStreamProcessor {
  public void runFlow(StreamExecutionEnvironment env) throws Exception {
    // Get source data
    DataStream<VehicleEvent> vehicleEvents = env
        .fromCollection(TrafficEventGenerator.generateTrafficEventList())
        .assignTimestampsAndWatermarks(
            WatermarkStrategy
                // TODO: Figure out why Type Param is required here?
                // TODO: Explore Watermark strategies.
                .<VehicleEvent>forBoundedOutOfOrderness(  // WatermarkGenerator requires event type for type checking.
                    Duration.ofSeconds(5))  // Define watermark delay delta
                                            // Watermark timestamp = the newest event timestamp seen - watermark delay delta
                                            // Discard events that are 5 or more seconds past the newest event timestamp seen.
                .withTimestampAssigner(new VehicleEventTimestampAssigner())
        );

    // Alter stream (stream of event type, window type (TimeWindow vs GlobalWindow))
    AllWindowedStream<VehicleEvent, TimeWindow> windowedVehicleEvents =
        vehicleEvents.windowAll(  // Get AllWindowStream
            TumblingEventTimeWindows.of(  // Choose WindowAssigner
                // TODO: Figure out why Flink's Time class is required here?
                Time.minutes(1) // Window size. Must use Flink's Time class
            )); // Window evaluation gets triggered when the window closes (default behavior)

    // Transform events
    windowedVehicleEvents
        .apply(new TimeBasedWindowedAverageFunction())
        .map(TrafficMeterEvent::toString)
        .print();

    env.execute();
  }
}
