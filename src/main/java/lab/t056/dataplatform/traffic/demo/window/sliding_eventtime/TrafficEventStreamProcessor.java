package lab.t056.dataplatform.traffic.demo.window.sliding_eventtime;

import lab.t056.dataplatform.traffic.entity.event.TrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.operator.timestampassigner.VehicleEventTimestampAssigner;
import lab.t056.dataplatform.traffic.operator.transformation.TimeBasedWindowedAverageFunction;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
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
                .<VehicleEvent>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                .withTimestampAssigner(new VehicleEventTimestampAssigner())
        );

    // Alter stream (stream of event type, window type (TimeWindow vs GlobalWindow))
    AllWindowedStream<VehicleEvent, TimeWindow> windowedVehicleEvents =
        vehicleEvents.windowAll(
            SlidingEventTimeWindows.of(
                Time.minutes(1),  // Window size
                Time.seconds(10)  // Window opening frequency
            ));

    // Transform events
    windowedVehicleEvents
        .apply(new TimeBasedWindowedAverageFunction())
        .map(TrafficMeterEvent::toString)
        .print();

    env.execute();
    // Look for meter event per 10 seconds looking back 1 minute respective of the window opening time .
  }
}