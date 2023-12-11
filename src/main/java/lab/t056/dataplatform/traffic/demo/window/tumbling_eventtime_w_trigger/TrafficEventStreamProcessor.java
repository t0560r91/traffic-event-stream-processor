package lab.t056.dataplatform.traffic.demo.window.tumbling_eventtime_w_trigger;

import lab.t056.dataplatform.traffic.entity.event.TrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.component.timestampassigner.VehicleEventTimestampAssigner;
import lab.t056.dataplatform.traffic.component.transformationfunction.TimeBasedWindowedAverageFunction;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
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

    // Alter stream
    AllWindowedStream<VehicleEvent, TimeWindow> windowedVehicleEvents =
        vehicleEvents
            .windowAll(TumblingEventTimeWindows.of(Time.minutes(1)))
            // TODO: Learn more about custom Trigger.
            .trigger(CountTrigger.of(1)); // Define additional trigger to evaluate window intermittently
                                                    // Default trigger evaluates window at close time

    // Transform events
    windowedVehicleEvents
        .apply(new TimeBasedWindowedAverageFunction())
        .map(TrafficMeterEvent::toString)
        .print();

    env.execute();
    // Look for meter event for every vehicle event looking from the beginning of the window until when the trigger fires.
  }
}
