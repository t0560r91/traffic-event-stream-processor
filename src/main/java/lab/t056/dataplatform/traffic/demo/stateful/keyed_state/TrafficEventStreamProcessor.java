package lab.t056.dataplatform.traffic.demo.stateful.keyed_state;

import lab.t056.dataplatform.traffic.entity.event.TrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.component.keyselector.VehicleTypeKeySelector;
import lab.t056.dataplatform.traffic.component.transformationfunction.StatefulTrafficMeterProcessFunction;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
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
