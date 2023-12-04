package lab.t056.dataplatform.traffic.archive;

import lab.t056.dataplatform.traffic.entity.enums.DriverLabel;
import lab.t056.dataplatform.traffic.entity.event.LabeledVehicleEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class LabelVehicleEventByVehicleTypeWithState
    extends KeyedProcessFunction<String, VehicleEvent, LabeledVehicleEvent> {

  ValueState<Integer> stateCount;
  ValueState<Float> stateRunningAvgSpeed;

  @Override
  public void processElement(VehicleEvent trafficEvent,
                             KeyedProcessFunction<String, VehicleEvent, LabeledVehicleEvent>.Context context,
                             Collector<LabeledVehicleEvent> collector) throws Exception {

    // Initialize states
    if (stateCount.value() == null) {
      stateCount.update(0);
    }
    if (stateRunningAvgSpeed.value() == null) {
      stateRunningAvgSpeed.update((float) 0);
    }

    // Get new states
    int newStateCount = stateCount.value() + 1;
    float newStateRunningAvgState = (float)(stateRunningAvgSpeed.value() + trafficEvent.getSpeed()) / newStateCount;

    stateCount.update(newStateCount);
    stateRunningAvgSpeed.update(newStateRunningAvgState);

    DriverLabel label;
    if (trafficEvent.getSpeed() > newStateRunningAvgState) {
      label = DriverLabel.FASTER_DRIVER;
    } else if (trafficEvent.getSpeed() == newStateRunningAvgState) {
      label = DriverLabel.NORMAL_DRIVER;
    } else {
      label = DriverLabel.SLOWER_DRIVER;
    }

//    collector.collect(
//        new LabeledVehicleEvent(
//            trafficEvent.getVehicle().getClass(),
//            trafficEvent.getSpeed(),
//            trafficEvent.getEventTimestamp(),
//            label,
//            trafficEvent.getPassengers(),
//            context.getCurrentKey(),
//            null,
//            null
//        )
//    );
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    stateCount
        = getRuntimeContext().getState(new ValueStateDescriptor<>("count", Integer.class));
    stateRunningAvgSpeed
        = getRuntimeContext().getState(new ValueStateDescriptor<>("running-avg-speed", Float.class));
  }

  @Override
  public void close() throws Exception {
    if (stateCount != null) {
      stateCount.clear();
    }

    if (stateRunningAvgSpeed != null) {
      stateRunningAvgSpeed.clear();
    }
  }
}
