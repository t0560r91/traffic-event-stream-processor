package lab.t056.dataplatform.traffic.archive;

import lab.t056.dataplatform.traffic.entity.event.LabeledVehicleEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class LabelVehicleEventByVehicleType
    extends KeyedProcessFunction<String, VehicleEvent, LabeledVehicleEvent> {
  @Override
  public void processElement(VehicleEvent e,
                             KeyedProcessFunction<String, VehicleEvent, LabeledVehicleEvent>.Context context,
                             Collector<LabeledVehicleEvent> collector) throws Exception {

//    collector.collect(
//        new LabeledVehicleEvent(
//            e.getVehicle().getClass();
//            e.getSpeed(),
//            e.getEventTimestamp(),
//            DriverLabel.NORMAL_DRIVER,
//            e.getPassengers(),
//            context.getCurrentKey(),
//            null,
//            null));
  }
}