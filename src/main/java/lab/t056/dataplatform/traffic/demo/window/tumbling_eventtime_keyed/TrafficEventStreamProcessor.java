package lab.t056.dataplatform.traffic.demo.window.tumbling_eventtime_keyed;

import lab.t056.dataplatform.traffic.entity.event.PerVehicleTypeTrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.operator.keyselector.VehicleTypeKeySelector;
import lab.t056.dataplatform.traffic.operator.timestampassigner.VehicleEventTimestampAssigner;
import lab.t056.dataplatform.traffic.operator.transformation.TimeBasedKeyedWindowedAverageFunction;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.time.Duration;


public class TrafficEventStreamProcessor {
  public void runFlow(StreamExecutionEnvironment env) throws Exception {
    // Get source data and define watermark strategy (watermark generator, timestamp assigner)
    // TODO: Think about why watermark strategy is set on the DataStreamSource?
    DataStream<VehicleEvent> vehicleEvents =
        env
            .fromCollection(TrafficEventGenerator.generateTrafficEventList())
            .assignTimestampsAndWatermarks(
                WatermarkStrategy
                    .<VehicleEvent>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                    .withTimestampAssigner(new VehicleEventTimestampAssigner())
            );

    // Alter stream
    WindowedStream<VehicleEvent, String, TimeWindow> windowedVehicleEvents =
        vehicleEvents
            .keyBy(new VehicleTypeKeySelector())
            .window(TumblingEventTimeWindows.of(Time.minutes(1)));

    // Transform events
    windowedVehicleEvents
        .apply(new TimeBasedKeyedWindowedAverageFunction())
        .map(PerVehicleTypeTrafficMeterEvent::toString)
        .print();

    env.execute();
  }
}
