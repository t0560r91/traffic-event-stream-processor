package lab.t056.dataplatform.traffic.demo.essential.reduce;

import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.component.keyselector.VehicleTypeKeySelector;
import lab.t056.dataplatform.traffic.component.transformationfunction.MaxSpeedVehicleEventReducerFunction;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TrafficEventStreamProcessor {
  public void runFlow(StreamExecutionEnvironment env) throws Exception {
    DataStream<VehicleEvent> vehicleEvents = env.fromCollection(TrafficEventGenerator.generateTrafficEventList());

    vehicleEvents.keyBy(new VehicleTypeKeySelector())
        .reduce(new MaxSpeedVehicleEventReducerFunction())
        .map(VehicleEvent::toString)
        .print();

    env.execute();
  }
}
