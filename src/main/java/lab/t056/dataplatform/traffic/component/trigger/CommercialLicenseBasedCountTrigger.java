package lab.t056.dataplatform.traffic.component.trigger;

import lab.t056.dataplatform.traffic.entity.enums.LicenseType;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

// Trigger can access Persistent States as well.

public class CommercialLicenseBasedCountTrigger extends Trigger<VehicleEvent, GlobalWindow> {

  ValueState<Long> stateCommercialVehicleCounts;
  Long nextCounts;

  @Override
  public TriggerResult onElement(VehicleEvent element,
                                 long timestamp,
                                 GlobalWindow window,
                                 TriggerContext ctx) throws Exception {

    if (element.getVehicle().getLicenseType() != null
        && element.getVehicle().getLicenseType() == LicenseType.COMMERCIAL) {

      stateCommercialVehicleCounts = ctx.getPartitionedState(
          new ValueStateDescriptor<Long>("commercial-vehicle-counts", TypeInformation.of(Long.class))
      );

      if (stateCommercialVehicleCounts.value() == null) {
        stateCommercialVehicleCounts.update(0L);
      }

      nextCounts = stateCommercialVehicleCounts.value() + 1;
      stateCommercialVehicleCounts.update(nextCounts);

      if (nextCounts >= 3) {
        stateCommercialVehicleCounts.update(0L);  // reset state
        return TriggerResult.FIRE_AND_PURGE;
      } else {
        return TriggerResult.CONTINUE;
      }
    } else {
      return TriggerResult.CONTINUE;
    }
  }

  @Override
  public TriggerResult onProcessingTime(long time, GlobalWindow window, TriggerContext ctx) throws Exception {
    return null;
  }

  @Override
  public TriggerResult onEventTime(long time, GlobalWindow window, TriggerContext ctx) throws Exception {
    return null;
  }

  @Override
  public void clear(GlobalWindow window, TriggerContext ctx) throws Exception {

  }
}