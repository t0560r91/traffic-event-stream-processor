package lab.t056.dataplatform.traffic.demo.window.global_keyed;

import lab.t056.dataplatform.traffic.entity.event.PerVehicleTypeTrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.operator.keyselector.VehicleTypeKeySelector;
import lab.t056.dataplatform.traffic.operator.transformation.CountBasedWindowedAverageFunction;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
import org.apache.flink.streaming.api.windowing.triggers.PurgingTrigger;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;


public class TrafficEventStreamProcessor {
  public void runFlow(StreamExecutionEnvironment env) throws Exception {
    // Get source data
    DataStream<VehicleEvent> vehicleEvents = env
        .addSource(new TrafficEventGenerator(3, 1));

    // Alter stream (stream of event type, window type (TimeWindow vs GlobalWindow))
    WindowedStream<VehicleEvent, String, GlobalWindow> windowedVehicleEvents =
        vehicleEvents
            .keyBy(new VehicleTypeKeySelector())
            .window(GlobalWindows.create())
            .trigger(PurgingTrigger.of(CountTrigger.of(2)));

    // Transform events
    windowedVehicleEvents
        .process(new CountBasedWindowedAverageFunction())
        .map(PerVehicleTypeTrafficMeterEvent::toString)
        .print();

    env.execute();

  }
}
