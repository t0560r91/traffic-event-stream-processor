package lab.t056.dataplatform.traffic.demo.essential.filter;

import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.component.transformationfunction.TracktorTrailerFilterFunction;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Use case: Stream Application | Data pipeline | Low latency analytics
 * Data Source:
 *  - VehicleEvent
 * Processes:
 *  - [App][Real-time] Emit adaptive-speed-limit to the speed limit display service
 *  - [Analytic][Real-time] Update adaptive-speed-limit based on real-time traffic
 *  - [Analytic][Real-time] Determine DriverLabel and label the event
 *  - [Analytic] Generate PassengerEvent
 * Data Sink:
 *  - PassengerEvent log
 *  - VehicleEvent log
 *  - LabeledVehicleEvent log
 * Ingest VehicleEvent
 *
 * Generate PassengerEvent for analytics
 *
 */
public class TrafficEventStreamProcessor {
  public void runFlow(StreamExecutionEnvironment env) throws Exception {
    DataStream<VehicleEvent> vehicleEvents = env.fromCollection(TrafficEventGenerator.generateTrafficEventList());

    vehicleEvents.filter(new TracktorTrailerFilterFunction())
        .map(VehicleEvent::toString)
        .print();

    env.execute();
  }
}
