package lab.t056.dataplatform.traffic.component.transformationfunction;

import lab.t056.dataplatform.traffic.entity.event.PerVehicleTypeTrafficMeterEvent;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.util.UUID;

public class StatefulTrafficMeterProcessFunction
    extends KeyedProcessFunction<String, VehicleEvent, PerVehicleTypeTrafficMeterEvent> {

  private ValueState<Long> stateCounts;
  private ValueState<Double> stateAvgSpeed;
  private ValueState<Double> stateMaxSpeed;
  private ValueState<Double> stateMinSpeed;

  @Override
  public void processElement(VehicleEvent event,
                             Context context,
                             Collector<PerVehicleTypeTrafficMeterEvent> collector) throws Exception {

    Long prevCounts;
    Double prevAvgSpeed;
    Double prevMaxSpeed;
    Double prevMinSpeed;

    // Get previous state values.
    if (stateCounts.value() == null) {
      prevCounts = 0L;
      prevAvgSpeed = 0.0;
      prevMaxSpeed = Double.MIN_VALUE;
      prevMinSpeed = Double.MAX_VALUE;
    } else {
      prevCounts = stateCounts.value();
      prevAvgSpeed = stateAvgSpeed.value();
      prevMaxSpeed = stateMaxSpeed.value();
      prevMinSpeed = stateMinSpeed.value();
    }

    // Calculate new state values.
    var nextCounts = prevCounts + 1;
    var nextAvgSpeed = (prevAvgSpeed * prevCounts + event.getSpeed()) / nextCounts;
    var nextMaxSpeed = Math.max(prevMaxSpeed, event.getSpeed());
    var nextMinSpeed = Math.min(prevMinSpeed, event.getSpeed());

    // Put new state values.
    stateCounts.update(nextCounts);
    stateAvgSpeed.update(nextAvgSpeed);
    stateMaxSpeed.update(nextMaxSpeed);
    stateMinSpeed.update(nextMinSpeed);

    collector.collect(
        new PerVehicleTypeTrafficMeterEvent(
            UUID.randomUUID(),
            Instant.now(),
            context.getCurrentKey(),
            nextCounts,
            nextAvgSpeed,
            nextMaxSpeed,
            nextMinSpeed,
            null,
            null,
            null,
            null
        )
    );
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    RuntimeContext ctx = getRuntimeContext();

    stateCounts = ctx.getState(
        new ValueStateDescriptor<>(
            "state1",
            TypeInformation.of(Long.class)
            // TODO: Learn about TypeInformation, TypeHint and figure out why it is needed here.
        )
    );
    stateAvgSpeed = ctx.getState(
        new ValueStateDescriptor<>(
            "state2",
            TypeInformation.of(Double.class)
        )
    );
    stateMaxSpeed = ctx.getState(
        new ValueStateDescriptor<>(
            "state3",
            TypeInformation.of(Double.class)
        )
    );
    stateMinSpeed = ctx.getState(
        new ValueStateDescriptor<>(
            "state4",
            TypeInformation.of(Double.class)
        )
    );
  }

  @Override
  public void close() throws Exception {
    stateCounts.clear();
    stateAvgSpeed.clear();
    stateMaxSpeed.clear();
    stateMinSpeed.clear();
  }


}
