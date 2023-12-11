package lab.t056.dataplatform.traffic.component.transformationfunction;

import lab.t056.dataplatform.traffic.entity.event.PassengerEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import java.util.UUID;

public class PassengerEventProducerFunction implements FlatMapFunction<VehicleEvent, PassengerEvent> {

  @Override
  public void flatMap(VehicleEvent vehicleEvent, Collector<PassengerEvent> collector) throws Exception {
    vehicleEvent.getPassengers().forEach(p -> {
      collector.collect(new PassengerEvent(  // TODO: Figure out objects being copied in the memory by collect method.
          UUID.randomUUID(),
          vehicleEvent.getEventTimestamp(),
          p,
          vehicleEvent.getVehicle(),
          vehicleEvent.getEventUUID()
      ));
    });
  }
}
