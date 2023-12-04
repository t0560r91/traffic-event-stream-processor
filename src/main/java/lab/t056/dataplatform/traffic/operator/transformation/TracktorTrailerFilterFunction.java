package lab.t056.dataplatform.traffic.operator.transformation;

import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.entity.thing.TractorTrailer;
import org.apache.flink.api.common.functions.FilterFunction;

public class TracktorTrailerFilterFunction implements FilterFunction<VehicleEvent> {

  @Override
  public boolean filter(VehicleEvent value) throws Exception {
    return !(value.getVehicle() instanceof TractorTrailer);
  }
}