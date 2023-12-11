//package lab.t056.dataplatform.traffic.component.evictor;
//
//import lab.t056.dataplatform.traffic.entity.enums.LicenseType;
//import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
//import org.apache.flink.streaming.api.windowing.evictors.Evictor;
//import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
//import org.apache.flink.streaming.runtime.operators.windowing.TimestampedValue;
//
//import java.util.ArrayList;
//
//// Order of events is not guaranteed.
//// Pre-aggregation is prevented.
//public class CustomEvictor implements Evictor<VehicleEvent, GlobalWindow> {
//  @Override
//  public void evictBefore(Iterable<TimestampedValue<VehicleEvent>> elements,
//                          int size,
//                          GlobalWindow window,
//                          EvictorContext evictorContext) {
//
//    boolean prev1 = false;
//    boolean prev2 = false;
//    boolean prev3 = false;
//
//    ArrayList<VehicleEvent> kept = new ArrayList<>();
//    for (TimestampedValue<VehicleEvent> e : elements) {
//      elements.
//      if (prev1 || prev2 || prev3) {
//        kept.add(e.getValue());
//      }
//      // Update for the next iteration.
//      prev3 = prev2;
//      prev2 = prev1;
//      prev1 = e.getValue().getVehicle().getLicenseType() == LicenseType.COMMERCIAL;
//
//    }
//
//  }
//
//  @Override
//  public void evictAfter(Iterable<TimestampedValue<VehicleEvent>> elements,
//                         int size,
//                         GlobalWindow window,
//                         EvictorContext evictorContext) {
//
//  }
//}
