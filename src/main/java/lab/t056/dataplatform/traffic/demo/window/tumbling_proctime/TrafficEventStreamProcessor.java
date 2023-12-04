package lab.t056.dataplatform.traffic.demo.window.tumbling_proctime;

import lab.t056.dataplatform.traffic.entity.event.TrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.operator.transformation.TimeBasedWindowedAverageFunction;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;



public class TrafficEventStreamProcessor {
  public void runFlow(StreamExecutionEnvironment env) throws Exception {
    // Get source data
    DataStream<VehicleEvent> vehicleEvents = env
        .addSource(new TrafficEventGenerator(3, 1))
//        .assignTimestampsAndWatermarks(
//            WatermarkStrategy.<VehicleEvent>forBoundedOutOfOrderness(Duration.ofSeconds(1))
//                .withTimestampAssigner(new VehicleEventTimestampAssigner())
//        );
    ;

    // Alter stream (stream of event type, window type (TimeWindow vs GlobalWindow))
    AllWindowedStream<VehicleEvent, TimeWindow> windowedVehicleEvents =
        vehicleEvents
            .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(10)));
//            .windowAll(TumblingEventTimeWindows.of(Time.seconds(10)));

    // Transform events
    windowedVehicleEvents
        .apply(new TimeBasedWindowedAverageFunction())
        .map(TrafficMeterEvent::toString)
        .print();

    env.execute();

    // Look for the events assigned to windows based in the processing timestamp.
    // And also thw windowing works without defining WatermarkStrategy nor TimeestampAssigner.
    // This is in contrast with the windows when used in event timestamp.
  }
}
