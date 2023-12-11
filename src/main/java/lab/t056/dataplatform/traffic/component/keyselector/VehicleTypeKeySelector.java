package lab.t056.dataplatform.traffic.component.keyselector;

import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.api.java.functions.KeySelector;

public class VehicleTypeKeySelector implements KeySelector<VehicleEvent, String> {
  @Override
  public String getKey(VehicleEvent e) throws Exception {
    return e.getVehicle().getClass().getSimpleName();
  }
}
