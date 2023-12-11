package lab.t056.dataplatform.traffic.demo.window.global_keyed_w_customtrigger;

import lab.t056.dataplatform.traffic.component.keyselector.VehicleTypeKeySelector;
import lab.t056.dataplatform.traffic.component.transformationfunction.CountBasedWindowedAverageFunction;
import lab.t056.dataplatform.traffic.component.trigger.CommercialLicenseBasedCountTrigger;
import lab.t056.dataplatform.traffic.entity.event.PerVehicleTypeTrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;


public class TrafficEventStreamProcessor {
  public void runFlow(StreamExecutionEnvironment env) throws Exception {
    // Get source data
    DataStream<VehicleEvent> vehicleEvents = env
        .addSource(new TrafficEventGenerator(3, 1));


    // Transform events
    vehicleEvents
        .keyBy(new VehicleTypeKeySelector())
        .window(
            GlobalWindows.create()
        )
        .trigger(new CommercialLicenseBasedCountTrigger())
        .process(new CountBasedWindowedAverageFunction())
        .map(PerVehicleTypeTrafficMeterEvent::toString)
        .print();

    env.execute();
    // Look for meter events aggregating all tumbling vehicles by vehicle type for every 3 commercial license type.
  }
}
