package lab.t056.dataplatform.traffic.archive;

import lab.t056.dataplatform.traffic.entity.event.LabeledVehicleEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class LabelVehicleEvent extends ProcessFunction<VehicleEvent, LabeledVehicleEvent> {
  @Override
  public void processElement(VehicleEvent trafficEvent,
                             ProcessFunction<VehicleEvent, LabeledVehicleEvent>.Context context,
                             Collector<LabeledVehicleEvent> collector) throws Exception {
//    collector.collect(
//        new LabeledVehicleEvent(
//            trafficEvent.getVehicleType(),
//            trafficEvent.getSpeed(),
//            trafficEvent.getEventTimestamp(),
//            DriverLabel.NORMAL_DRIVER,
//            trafficEvent.getPassengers(),
//            context.toString(),
//            null,
//            null));
  }
}
