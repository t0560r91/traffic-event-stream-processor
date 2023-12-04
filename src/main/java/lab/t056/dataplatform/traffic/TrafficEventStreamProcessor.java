package lab.t056.dataplatform.traffic;

import lab.t056.dataplatform.traffic.entity.event.TrafficMeterEvent;
import lab.t056.dataplatform.traffic.operator.keyselector.VehicleTypeKeySelector;
import lab.t056.dataplatform.traffic.operator.transformation.StatefulTrafficMeterProcessFunction;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class TrafficEventStreamProcessor {
  public void runFlow(StreamExecutionEnvironment env) throws Exception {
    // Get source data
    DataStream<VehicleEvent> vehicleEvents = env
        .addSource(new TrafficEventGenerator(3, 1));


    // Transform events
    vehicleEvents
        .keyBy(new VehicleTypeKeySelector())
        .process(new StatefulTrafficMeterProcessFunction())
        .map(TrafficMeterEvent::toString)
        .print();

    env.execute();
    // Look for running statistics per vehicle type.
  }
}
