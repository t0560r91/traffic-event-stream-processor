package lab.t056.dataplatform.traffic.operator.transformation;

import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.api.common.functions.ReduceFunction;

public class MaxSpeedVehicleEventReducerFunction implements ReduceFunction<VehicleEvent> {
  @Override
  public VehicleEvent reduce(VehicleEvent e1, VehicleEvent e2) throws Exception {
    return e1.getSpeed() >= e2.getSpeed() ? e1 : e2;
  }
}
